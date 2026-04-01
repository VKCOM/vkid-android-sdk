@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http.captcha

import com.vk.id.captcha.api.VKCaptcha
import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.http.HttpRequest
import com.vk.id.network.http.HttpResponse
import com.vk.id.network.http.Interceptor
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.atomic.AtomicReference

/**
 * An interceptor to handle error 14 from API.
 *
 * The interceptor will handle necessary captcha requests and retry api calls.
 *
 * @property domains The set of domains on which to handle captcha. If empty, will be applied to all domains.
 */
internal class Error14HandlingInterceptor(
    private val domains: Set<String> = emptySet(),
) : Interceptor {

    private val cookie = AtomicReference<String?>(null)

    private companion object {
        private const val CAPTCHA_ERROR_CODE = 14
    }

    /** @suppress */
    @Suppress("ReturnCount")
    override suspend fun intercept(chain: Interceptor.Chain): HttpResponse {
        val request = chain.request().withCookie()
        val response = chain.proceed(request)
        response.parseCookie()
        if (request.shouldSkipCaptcha()) return response
        val redirectUri = response.getRedirectUri() ?: return response
        val token = passCaptchaAndGetToken(redirectUri)
        return chain.proceed(chain.request().withCookie().withSuccessToken(token))
    }

    private suspend fun passCaptchaAndGetToken(redirectUri: String): String {
        val url = URL(redirectUri)
        val domain = "${url.protocol}://${url.host}"
        val result = VKCaptcha.openCaptchaSuspended(
            domain = domain,
            redirectUri = redirectUri
        )
        return result.toResult().getOrThrow()
    }

    private fun HttpRequest.withSuccessToken(token: String): HttpRequest {
        return newBuilder()
            .url(urlBuilder().addQueryParameter("success_token", token).build())
            .build()
    }

    @Suppress("ReturnCount")
    private fun HttpResponse.getRedirectUri(): String? {
        val responseBody = try {
            peekBody(Long.MAX_VALUE)?.string ?: return null
        } catch (_: JSONException) {
            return null
        }.let { JSONObject(it) }

        if (!responseBody.has("error")) {
            return null
        }

        val error = responseBody.getJSONObject("error")
        return if (error.getInt("error_code") == CAPTCHA_ERROR_CODE) {
            error.getString("redirect_uri")
        } else {
            null
        }
    }

    private fun HttpRequest.shouldSkipCaptcha(): Boolean {
        val requestUrl = URL(url)
        return !domains.contains(requestUrl.host) && domains.isNotEmpty()
    }

    private fun HttpResponse.parseCookie() {
        headerValues("Set-Cookie").firstOrNull { it.contains("remixstlid") }?.let(cookie::set)
    }

    private fun HttpRequest.withCookie(): HttpRequest {
        val builder = newBuilder()
        cookie.get()?.let { builder.header("Cookie", it) }
        return builder.build()
    }
}
