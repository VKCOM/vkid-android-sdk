package com.vk.id.internal.api.sslpinning.api

import android.content.Context
import com.vk.id.internal.api.sslpinning.okhttp.security.SSLCertificateStore
import com.vk.id.internal.api.sslpinning.okhttp.trust.SSLTrustManager

internal class DefaultSSLTrustManagerProvider(
    private val context: Context,
    private val certificateStore: SSLCertificateStore,
    private val reuseSslSocketFactory: Boolean = false,
) {

    private val lazyTrustManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        SSLTrustManager(context, certificateStore)
    }

    fun provideManager(): SSLTrustManager =
        if (reuseSslSocketFactory) {
            lazyTrustManager
        } else {
            SSLTrustManager(context, certificateStore)
        }
}
