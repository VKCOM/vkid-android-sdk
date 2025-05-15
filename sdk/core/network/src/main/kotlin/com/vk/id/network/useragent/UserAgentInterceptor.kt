package com.vk.id.network.useragent

import com.vk.id.captcha.api.VKCaptcha
import okhttp3.Interceptor
import okhttp3.Response

internal class UserAgentInterceptor(
    private val userAgentProvider: UserAgentProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        VKCaptcha.setUserAgent(userAgentProvider.userAgent)
        val requestWithUserAgent = originalRequest.newBuilder()
            .header("User-Agent", userAgentProvider.userAgent)
            .build()
        return chain.proceed(requestWithUserAgent)
    }
}
