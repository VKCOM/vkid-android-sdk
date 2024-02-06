package com.vk.id.internal.api.useragent

import okhttp3.Interceptor
import okhttp3.Response

internal class UserAgentInterceptor(
    private val userAgentProvider: UserAgentProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithUserAgent = originalRequest.newBuilder()
            .header("User-Agent", userAgentProvider.userAgent)
            .build()
        return chain.proceed(requestWithUserAgent)
    }
}
