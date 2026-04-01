@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.useragent

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.http.HttpResponse
import com.vk.id.network.http.Interceptor

internal class UserAgentInterceptor(
    private val userAgentProvider: UserAgentProvider
) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): HttpResponse {
        val originalRequest = chain.request()
        val requestWithUserAgent = originalRequest.newBuilder()
            .header("User-Agent", userAgentProvider.userAgent)
            .build()
        return chain.proceed(requestWithUserAgent)
    }
}
