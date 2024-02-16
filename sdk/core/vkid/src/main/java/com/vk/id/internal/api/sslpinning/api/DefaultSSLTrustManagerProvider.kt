package com.vk.id.internal.api.sslpinning.api

import android.content.Context
import com.vk.id.internal.api.sslpinning.okhttp.security.SSLCertificateStore
import com.vk.id.internal.api.sslpinning.okhttp.trust.SSLTrustManager

internal class DefaultSSLTrustManagerProvider(
    private val context: Context
) {

    private lateinit var certificateStore: SSLCertificateStore
    private var reuseSslSocketFactory = false

    private val lazyTrustManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        SSLTrustManager(context, certificateStore)
    }

    fun init(store: SSLCertificateStore, reuseSslSocketFactory: Boolean) {
        this.certificateStore = store
        this.reuseSslSocketFactory = reuseSslSocketFactory
    }

    fun provideManager(): SSLTrustManager =
        if (reuseSslSocketFactory) {
            lazyTrustManager
        } else {
            SSLTrustManager(context, certificateStore)
        }
}
