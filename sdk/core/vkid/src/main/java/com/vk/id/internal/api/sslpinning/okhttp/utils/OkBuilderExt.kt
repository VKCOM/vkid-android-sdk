package com.vk.id.internal.api.sslpinning.okhttp.utils

import android.os.Looper
import com.vk.id.internal.api.sslpinning.SSLFactoryProvider
import com.vk.id.internal.log.createLoggerForClass
import okhttp3.OkHttpClient

internal fun OkHttpClient.Builder.addSslSocketFactory(
    provider: SSLFactoryProvider
): OkHttpClient.Builder {
    val logger = createLoggerForClass()
    if (Looper.getMainLooper() == Looper.myLooper()) {
        logger.error("error! don't call from main thread!", null)
    }
    @Suppress("TooGenericExceptionCaught")
    try {
        sslSocketFactory(provider.factory, provider)
    } catch (e: Exception) {
        logger.error("", e)
    }
    return this
}
