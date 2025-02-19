@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.annotation.VisibleForTesting
import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.internalVKIDCreateLoggerForClass
import com.vk.id.network.useragent.UserAgentInterceptor
import com.vk.id.network.useragent.UserAgentProvider
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.BufferedInputStream
import java.util.concurrent.TimeUnit

internal class OkHttpClientProvider(
    private val context: Context
) {
    fun provide() = provideBuilderWithSslPinning()
        .readTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .connectTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor())
        .addInterceptor(UserAgentInterceptor(UserAgentProvider(context)))
        .apply { InternalVKIDAdditionalInterceptors.getInterceptor()?.let(::addNetworkInterceptor) }
        .build()

    @VisibleForTesting
    fun provideBuilderWithSslPinning(): OkHttpClient.Builder {
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
        private const val HOST_NAME_API = "*.vk.com"
        private const val OKHTTP_TIMEOUT_SECONDS = 60L
    }
}
