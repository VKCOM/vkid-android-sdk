@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http.ssl

import android.content.Context
import android.content.res.Resources
import android.os.Build
import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.InternalVKIDLog
import com.vk.id.network.R
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager

internal class SSLConfigurator(
    private val context: Context
) {

    private val logger = InternalVKIDLog.createLoggerForTag("SSLConfigurator")

    private val sslSocketFactory: SSLSocketFactory by lazy {
        val sslContext = createSSLContext()
        sslContext.init(emptyArray(), createTrustManagers(), SecureRandom())
        sslContext.socketFactory
    }

    public fun configureSSL(connection: HttpURLConnection) {
        if (connection !is HttpsURLConnection) {
            logger.debug("Skipping SSL configuration for non-HTTPS connection")
            return
        }

        try {
            connection.sslSocketFactory = sslSocketFactory
        } catch (e: KeyManagementException) {
            logger.error("Failed to initialize SSL context", e)
            throw IOException("Failed to configure SSL", e)
        }
    }

    private fun createSSLContext(): SSLContext {
        return try {
            val protocol = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Android 7.0+ поддерживает современные протоколы
                "TLSv1.3"
            } else {
                // Android 4.1+ может использовать TLS 1.2 с Conscrypt
                "TLSv1.2"
            }
            SSLContext.getInstance(protocol)
        } catch (_: NoSuchAlgorithmException) {
            // Fallback на TLS
            try {
                SSLContext.getInstance("TLS")
            } catch (e: NoSuchAlgorithmException) {
                logger.error("TLS algorithm not available", e)
                throw IOException("Failed to create SSL context", e)
            }
        }
    }

    private fun createTrustManagers(): Array<TrustManager> {
        return arrayOf(
            VkIdTrustManager(certificatePins = loadCertificatePins())
        )
    }

    private fun loadCertificatePins(): Set<String> {
        val pins = mutableSetOf<String>()
        try {
            context.resources.openRawResource(R.raw.vkid_cacerts_pins).use {
                BufferedInputStream(it)
                    .reader()
                    .readLines()
                    .forEach { pin ->
                        pins.add(pin)
                    }
            }
            logger.debug("Loaded ${pins.size} certificate pins")
        } catch (e: Resources.NotFoundException) {
            logger.error("Certificate pins not found in resources", e)
            throw IOException("Certificate pins not found in resources", e)
        } catch (e: IOException) {
            logger.error("Failed to read certificate pins file", e)
            throw IOException("Failed to load certificate pins from resources", e)
        }
        return pins
    }
}
