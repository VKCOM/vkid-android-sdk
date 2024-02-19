package com.vk.id.internal.api.sslpinning

import android.annotation.SuppressLint
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

@SuppressLint("CustomX509TrustManager")
internal interface SSLFactoryProvider : X509TrustManager {
    val factory: SSLSocketFactory
}
