@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http.ssl

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.InternalVKIDLog
import java.io.IOException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.MessageDigest
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.io.encoding.ExperimentalEncodingApi

@Suppress("CustomX509TrustManager", "TooManyFunctions")
internal class VkIdTrustManager(
    private val certificatePins: Set<String>,
) : X509TrustManager {

    private val logger = InternalVKIDLog.createLoggerForTag("VkIdTrustManager")

    private val systemTrustManager: X509TrustManager = createTrustManagerWithRuCA()

    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        systemTrustManager.checkClientTrusted(chain, authType)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("SwallowedException")
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        try {
            // SSL Pinning
            checkCertificatePinning(chain)
        } catch (e: CertificateException) {
            // Fallback - пробуем проверить System CA и Russian Trusted Root CA
            try {
                systemTrustManager.checkServerTrusted(chain, authType)
            } catch (ca: CertificateException) {
                // Оба способа не сработали
                logger.error("System CA verification failed", ca)
                throw ca
            }
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = systemTrustManager.acceptedIssuers

    @Throws(CertificateException::class)
    private fun checkCertificatePinning(chain: Array<X509Certificate>) {
        for (cert in chain) {
            if (computePublicKeyPin(cert) in certificatePins) {
                return
            }
        }

        // Ни один пин не совпал - выбрасываем исключение
        val errorDetails = buildString {
            appendLine("Certificate pinning failed.")
            append("Certificate chain pins: ")
            chain.forEach { cert ->
                append("[${computePublicKeyPin(cert)} (${cert.subjectDN})] ")
            }
        }

        logger.error(message = errorDetails, throwable = null)
        throw CertificateException(
            "Certificate pinning failed. None of the provided certificates match expected pins.",
        )
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun computePublicKeyPin(certificate: X509Certificate): String {
        val publicKey = certificate.publicKey.encoded
            ?: throw CertificateException("Failed to encode public key for certificate: ${certificate.subjectDN}")

        val hash = MessageDigest.getInstance("SHA-256").digest(publicKey)
        // Обычная реализация возвращает перенос строки, а он отсутствует в файле с пинами
        return Base64.encodeToString(hash, Base64.DEFAULT).trimIndent()
    }

    private fun createTrustManagerWithRuCA(): X509TrustManager {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)

        // Добавляем платформенные сертификаты
        addPlatformTrustedCertificates(keyStore)

        keyStore.setCertificateEntry("russian-root-ca", loadCertificate(RUSSIAN_ROOT_CA_PEM))

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(keyStore)

        return tmf.trustManagers[0] as X509TrustManager
    }

    private fun addPlatformTrustedCertificates(keyStore: KeyStore) {
        try {
            val platformTrustManager = createSystemTrustManager()
            platformTrustManager.acceptedIssuers.forEach { certificate ->
                keyStore.setCertificateEntry(certificate.subjectDN.toString(), certificate)
            }
        } catch (e: KeyStoreException) {
            logger.error("Failed to add platform trusted certificates", e)
            throw IOException("Failed to add platform trusted certificates", e)
        } catch (e: CertificateException) {
            logger.error("Failed to add platform trusted certificates", e)
            throw IOException("Failed to add platform trusted certificates", e)
        }
    }

    private fun loadCertificate(pem: String): X509Certificate {
        val cf = CertificateFactory.getInstance("X.509")
        return cf.generateCertificate(pem.byteInputStream()) as X509Certificate
    }

    @Suppress("TooGenericExceptionCaught")
    private fun createSystemTrustManager(): X509TrustManager {
        return try {
            getSystemTrustManager()
        } catch (e: Exception) {
            logger.error("Failed to create system trust manager", e)
            throw IOException("Failed to create system trust manager", e)
        }
    }

    private fun getSystemTrustManager(): X509TrustManager {
        val trustManagers = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            .run {
                // null означает использование системных CA по умолчанию
                init(null as KeyStore?)
                trustManagers
            }
        require(trustManagers.isNotEmpty() && trustManagers[0] is X509TrustManager) {
            "No X509TrustManager found in system trust managers"
        }
        return trustManagers[0] as X509TrustManager
    }

    private companion object {
        private val RUSSIAN_ROOT_CA_PEM
            get() = """-----BEGIN CERTIFICATE-----
MIIFwjCCA6qgAwIBAgICEAAwDQYJKoZIhvcNAQELBQAwcDELMAkGA1UEBhMCUlUx
PzA9BgNVBAoMNlRoZSBNaW5pc3RyeSBvZiBEaWdpdGFsIERldmVsb3BtZW50IGFu
ZCBDb21tdW5pY2F0aW9uczEgMB4GA1UEAwwXUnVzc2lhbiBUcnVzdGVkIFJvb3Qg
Q0EwHhcNMjIwMzAxMjEwNDE1WhcNMzIwMjI3MjEwNDE1WjBwMQswCQYDVQQGEwJS
VTE/MD0GA1UECgw2VGhlIE1pbmlzdHJ5IG9mIERpZ2l0YWwgRGV2ZWxvcG1lbnQg
YW5kIENvbW11bmljYXRpb25zMSAwHgYDVQQDDBdSdXNzaWFuIFRydXN0ZWQgUm9v
dCBDQTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAMfFOZ8pUAL3+r2n
qqE0Zp52selXsKGFYoG0GM5bwz1bSFtCt+AZQMhkWQheI3poZAToYJu69pHLKS6Q
XBiwBC1cvzYmUYKMYZC7jE5YhEU2bSL0mX7NaMxMDmH2/NwuOVRj8OImVa5s1F4U
zn4Kv3PFlDBjjSjXKVY9kmjUBsXQrIHeaqmUIsPIlNWUnimXS0I0abExqkbdrXbX
YwCOXhOO2pDUx3ckmJlCMUGacUTnylyQW2VsJIyIGA8V0xzdaeUXg0VZ6ZmNUr5Y
Ber/EAOLPb8NYpsAhJe2mXjMB/J9HNsoFMBFJ0lLOT/+dQvjbdRZoOT8eqJpWnVD
U+QL/qEZnz57N88OWM3rabJkRNdU/Z7x5SFIM9FrqtN8xewsiBWBI0K6XFuOBOTD
4V08o4TzJ8+Ccq5XlCUW2L48pZNCYuBDfBh7FxkB7qDgGDiaftEkZZfApRg2E+M9
G8wkNKTPLDc4wH0FDTijhgxR3Y4PiS1HL2Zhw7bD3CbslmEGgfnnZojNkJtcLeBH
BLa52/dSwNU4WWLubaYSiAmA9IUMX1/RpfpxOxd4Ykmhz97oFbUaDJFipIggx5sX
ePAlkTdWnv+RWBxlJwMQ25oEHmRguNYf4Zr/Rxr9cS93Y+mdXIZaBEE0KS2iLRqa
OiWBki9IMQU4phqPOBAaG7A+eP8PAgMBAAGjZjBkMB0GA1UdDgQWBBTh0YHlzlpf
BKrS6badZrHF+qwshzAfBgNVHSMEGDAWgBTh0YHlzlpfBKrS6badZrHF+qwshzAS
BgNVHRMBAf8ECDAGAQH/AgEEMA4GA1UdDwEB/wQEAwIBhjANBgkqhkiG9w0BAQsF
AAOCAgEAALIY1wkilt/urfEVM5vKzr6utOeDWCUczmWX/RX4ljpRdgF+5fAIS4vH
tmXkqpSCOVeWUrJV9QvZn6L227ZwuE15cWi8DCDal3Ue90WgAJJZMfTshN4OI8cq
W9E4EG9wglbEtMnObHlms8F3CHmrw3k6KmUkWGoa+/ENmcVl68u/cMRl1JbW2bM+
/3A+SAg2c6iPDlehczKx2oa95QW0SkPPWGuNA/CE8CpyANIhu9XFrj3RQ3EqeRcS
AQQod1RNuHpfETLU/A2gMmvn/w/sx7TB3W5BPs6rprOA37tutPq9u6FTZOcG1Oqj
C/B7yTqgI7rbyvox7DEXoX7rIiEqyNNUguTk/u3SZ4VXE2kmxdmSh3TQvybfbnXV
4JbCZVaqiZraqc7oZMnRoWrXRG3ztbnbes/9qhRGI7PqXqeKJBztxRTEVj8ONs1d
WN5szTwaPIvhkhO3CO5ErU2rVdUr89wKpNXbBODFKRtgxUT70YpmJ46VVaqdAhOZ
D9EUUn4YaeLaS8AjSF/h7UkjOibNc4qVDiPP+rkehFWM66PVnP1Msh93tc+taIfC
EYVMxjh8zNbFuoc7fzvvrFILLe7ifvEIUqSVIC/AzplM/Jxw7buXFeGP1qVCBEHq
391d/9RAfaZ12zkwFsl+IKwE/OZxW8AHa9i1p4GO0YSNuczzEm4=
-----END CERTIFICATE-----"""
    }
}
