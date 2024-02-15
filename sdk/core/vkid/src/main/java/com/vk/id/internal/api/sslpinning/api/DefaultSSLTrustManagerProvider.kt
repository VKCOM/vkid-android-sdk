package com.vk.id.internal.api.sslpinning.api

import com.vk.id.internal.api.sslpinning.okhttp.security.SSLCertificateStore
import com.vk.id.internal.api.sslpinning.okhttp.trust.SSLTrustManager

internal class DefaultSSLTrustManagerProvider {

    private lateinit var certificateStore: SSLCertificateStore
    private var reuseSslSocketFactory = false

    private val lazyTrustManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        SSLTrustManager(certificateStore)
    }

    fun init(store: SSLCertificateStore, reuseSslSocketFactory: Boolean) {
        this.certificateStore = store
        this.reuseSslSocketFactory = reuseSslSocketFactory
    }

    fun provideManager(): SSLTrustManager =
        if (reuseSslSocketFactory) {
            lazyTrustManager
        } else {
            SSLTrustManager(certificateStore)
        }
}
