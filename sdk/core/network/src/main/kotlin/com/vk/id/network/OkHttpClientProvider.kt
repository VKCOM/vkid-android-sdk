@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.annotation.VisibleForTesting
import com.vk.id.captcha.okhttp.api.CaptchaHandlingInterceptor
import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.internalVKIDCreateLoggerForClass
import com.vk.id.network.useragent.UserAgentInterceptor
import com.vk.id.network.useragent.UserAgentProvider
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.decodeCertificatePem
import java.io.BufferedInputStream
import java.util.concurrent.TimeUnit

@InternalVKIDApi
public class OkHttpClientProvider(
    private val context: Context
) {
    public fun provide(
        additionalInterceptors: List<Interceptor>
    ): OkHttpClient = provideBuilderWithSslPinning()
        .addRussianTrustedRootCA()
        .readTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .connectTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(CaptchaHandlingInterceptor())
        .apply { additionalInterceptors.forEach(::addInterceptor) }
        .addInterceptor(UserAgentInterceptor(UserAgentProvider(context)))
        .apply { InternalVKIDAdditionalInterceptors.getInterceptor()?.let(::addNetworkInterceptor) }
        .addInterceptor(loggingInterceptor())
        .build()

    private fun OkHttpClient.Builder.addRussianTrustedRootCA(): OkHttpClient.Builder {
        val certificate = """-----BEGIN CERTIFICATE-----
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
-----END CERTIFICATE-----""".decodeCertificatePem()

        val certificates: HandshakeCertificates = HandshakeCertificates.Builder()
            .addTrustedCertificate(certificate)
            .addPlatformTrustedCertificates()
            .build()

        return sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager)
    }

    @VisibleForTesting
    internal fun provideBuilderWithSslPinning(): OkHttpClient.Builder {
        val client = OkHttpClient.Builder()
        if (!isDebuggable()) {
            client.addVKPins()
        }
        return client
    }

    private fun OkHttpClient.Builder.addVKPins(): OkHttpClient.Builder {
        val builder = CertificatePinner.Builder()
        BufferedInputStream(context.resources.openRawResource(R.raw.vkid_cacerts_pins))
            .reader()
            .readLines()
            .map { "sha256/$it" }
            .forEach { pin -> builder.add(HOST_NAME_API, pin) }
        certificatePinner(builder.build())
        return this
    }

    private fun isDebuggable() = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    private fun loggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            private val logger = internalVKIDCreateLoggerForClass()
            override fun log(message: String) {
                logger.debug(message)
            }
        })
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    private companion object {
        private const val HOST_NAME_API = "*.vk.ru"
        private const val OKHTTP_TIMEOUT_SECONDS = 60L
    }
}
