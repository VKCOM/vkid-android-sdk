@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http.captcha

import com.vk.id.captcha.api.VKCaptcha
import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.http.HttpRequest
import com.vk.id.network.http.HttpResponse
import com.vk.id.network.http.Interceptor
import java.net.URL

/**
 * An interceptor to handle Hitman Challenge from API.
 *
 * The interceptor will handle necessary captcha requests and retry api calls.
 *
 * @property domains The set of domains on which to handle captcha. If empty, will be applied to all domains.
 */
internal class HitmanChallengeHandlingInterceptor(
    private val domains: Set<String> = emptySet(),
) : Interceptor {

    private companion object {
        private const val HEADER_CHALLENGE_SOLUTION = "X-Challenge-Solution"
        private const val HEADER_CHALLENGE_URL = "X-Challenge-Url"
        private const val HEADER_CHALLENGE = "X-Challenge"
    }

    @Suppress("ReturnCount")
    override suspend fun intercept(chain: Interceptor.Chain): HttpResponse {
        val domain = chain.request().getDomain()
        val request = chain.request().withSavedChallengeSolution()
        val response = chain.proceed(request)
        if (shouldSkipCaptcha(response, domain)) return response
        val challengeUrl = response.headerValues(HEADER_CHALLENGE_URL).firstOrNull() ?: return response
        val token = passCaptchaAndGetToken(domain, challengeUrl)
        return chain.proceed(chain.request().withChallengeSolution(token))
    }

    private suspend fun passCaptchaAndGetToken(
        domain: String,
        challengeUrl: String,
    ): String {
        val result = VKCaptcha.openCaptchaSuspended(
            domain = domain,
            redirectUri = domain + challengeUrl
        )
        return result.toResult().getOrThrow()
    }

    private fun HttpRequest.withChallengeSolution(token: String): HttpRequest {
        return newBuilder()
            .header(HEADER_CHALLENGE_SOLUTION, token)
            .build()
    }

    private fun HttpRequest.withSavedChallengeSolution(): HttpRequest {
        val domain = java.net.URL(url).run { "$protocol://$host" }
        val hitmanToken = VKCaptcha.getToken(domain)
        return if (this.header(HEADER_CHALLENGE_SOLUTION) == null && hitmanToken != null) {
            newBuilder().header(HEADER_CHALLENGE_SOLUTION, hitmanToken).build()
        } else {
            this
        }
    }

    private fun shouldSkipCaptcha(response: HttpResponse, domain: String): Boolean {
        return !domains.contains(domain) && domains.isNotEmpty() || response.headerValues(HEADER_CHALLENGE)
            .all { it != "required" }
    }

    private fun HttpRequest.getDomain(): String {
        val url = java.net.URL(this.url)
        return URL(url.protocol, url.host, url.port, "").toString()
    }
}
