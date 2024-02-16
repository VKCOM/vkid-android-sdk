package com.vk.id.internal.api.sslpinning.okhttp.trust

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.http.X509TrustManagerExtensions
import com.vk.id.internal.api.sslpinning.SSLFactoryProvider
import com.vk.id.internal.api.sslpinning.okhttp.security.SSLCertificateStore
import java.security.GeneralSecurityException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.Arrays
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@SuppressLint("CustomX509TrustManager")
internal open class SSLTrustManager(
    private val appContext: Context,
    certificateStore: SSLCertificateStore
) : SSLFactoryProvider {

    override val factory: SSLSocketFactory

    init {
        systemDefaultTrustManager(certificateStore).let {
            wrappedTrustManager = it
            trustManagerExtensions = X509TrustManagerExtensions(it)
        }

        factory = createSslSocketFactory(this)
    }

    private var wrappedTrustManager: X509TrustManager? = null
    private var trustManagers: Array<TrustManager>? = null
    private var trustManagerExtensions = wrappedTrustManager?.let {
        X509TrustManagerExtensions(it)
    }

    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>?, authType: String?) {
        wrappedTrustManager?.checkClientTrusted(chain, authType)
    }

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>?, authType: String?) {
        wrappedTrustManager?.checkServerTrusted(chain, authType)
    }

    @Suppress("unused")
    @Throws(CertificateException::class)
    open fun checkServerTrusted(
        certs: Array<X509Certificate>?,
        authType: String,
        hostname: String?
    ): List<X509Certificate> {
        return trustManagerExtensions?.checkServerTrusted(certs, authType, hostname)
            ?: certs?.toList()
            ?: emptyList()
    }

    override fun getAcceptedIssuers(): Array<X509Certificate>? {
        return wrappedTrustManager?.acceptedIssuers
    }

    private fun systemDefaultTrustManager(certificateStore: SSLCertificateStore): X509TrustManager {
        @Suppress("SwallowedException")
        try {
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())

            trustManagerFactory.init(certificateStore.keyStore.takeIf { !isDebuggable() })

            trustManagers = trustManagerFactory.trustManagers

            if (trustManagers!!.size != 1 || trustManagers!![0] !is X509TrustManager) {
                throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
            }

            return trustManagers!![0] as X509TrustManager
        } catch (e: GeneralSecurityException) {
            throw AssertionError() // The system has no TLS. Just give up.
        }
    }

    private fun isDebuggable() = appContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    private fun createSslSocketFactory(trustManager: TrustManager): SSLSocketFactory {
        @Suppress("TooGenericExceptionCaught")
        try {
            val context = SSLContext.getInstance("TLS")
            context.init(null, arrayOf(trustManager), null)
            context.clientSessionContext.sessionCacheSize = 0
            @Suppress("MagicNumber")
            context.clientSessionContext.sessionTimeout = 15 * 60 // 300 minutes
            return context.socketFactory
        } catch (e: Exception) {
            throw AssertionError(e)
        }
    }
}
