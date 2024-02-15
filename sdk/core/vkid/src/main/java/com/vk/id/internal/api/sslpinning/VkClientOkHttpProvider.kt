package com.vk.id.internal.api.sslpinning

import android.content.Context
import com.vk.id.internal.api.sslpinning.api.DefaultSSLTrustManagerProvider
import com.vk.id.internal.api.sslpinning.okhttp.security.SSLCertificateStore
import com.vk.id.internal.api.sslpinning.okhttp.security.SSLKeyStore
import com.vk.id.internal.api.sslpinning.okhttp.utils.addSslSocketFactory
import com.vk.id.internal.log.createLoggerForClass
import okhttp3.OkHttpClient

internal class VkClientOkHttpProvider(
    context: Context,
    isSslPinningEnabled: Boolean,
) {

    private var certificateStore: SSLCertificateStore
    private var defaultTrustManagerProvider = DefaultSSLTrustManagerProvider()

    private val defaultWebLoggerInitializationListener = object : SSLKeyStore.SSLKeyStoreInitializationListener {

        private val logger = createLoggerForClass()

        override fun onSSLKeyStoreInitializationComplete() = Unit

        override fun onSSLKeyStoreInitializationFailed(e: Throwable) {
            logger.error("${e.message}", e)
        }
    }

    init {
        val ketStore = SSLKeyStore(context = context, useDefaultHost = true)
            .apply { addInitializationListener(defaultWebLoggerInitializationListener) }

        val certStore = SSLCertificateStore(ketStore, isSslPinningEnabled)
        certificateStore = certStore

        defaultTrustManagerProvider.init(certificateStore, true)
    }

    fun addSslPinning(client: OkHttpClient.Builder): OkHttpClient.Builder {
        return client.addSslSocketFactory(defaultTrustManagerProvider.provideManager())
    }
}
