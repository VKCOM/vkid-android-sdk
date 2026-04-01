package com.vk.id.network

import android.content.Context
import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.http.HttpClient
import com.vk.id.network.http.Interceptor
import com.vk.id.network.http.LoggingInterceptor
import com.vk.id.network.http.RetryInterceptor
import com.vk.id.network.http.captcha.Error14HandlingInterceptor
import com.vk.id.network.http.captcha.HitmanChallengeHandlingInterceptor
import com.vk.id.network.useragent.UserAgentInterceptor
import com.vk.id.network.useragent.UserAgentProvider

@InternalVKIDApi
public class HttpClientProvider(private val context: Context) {

    public fun provide(additionalInterceptors: List<Interceptor> = emptyList()): HttpClient {
        val baseInterceptors = buildBaseInterceptors()
        return HttpClient(
            HttpClient.Config(
                context = context,
                connectingTimeout = DEFAULT_TIMEOUT,
                readingTimeout = DEFAULT_TIMEOUT,
                followRedirects = true,
                maxRedirects = DEFAULT_MAX_REDIRECTS,
                applicationInterceptors = baseInterceptors + additionalInterceptors
            )
        )
    }

    private fun buildBaseInterceptors(): List<Interceptor> {
        return listOfNotNull(
            UserAgentInterceptor(UserAgentProvider(context)),
            LoggingInterceptor("HttpClient"),
            RetryInterceptor(),
            Error14HandlingInterceptor(),
            HitmanChallengeHandlingInterceptor()
        )
    }

    private companion object {
        private const val DEFAULT_TIMEOUT = 60 * 1000
        private const val DEFAULT_MAX_REDIRECTS = 5
    }
}
