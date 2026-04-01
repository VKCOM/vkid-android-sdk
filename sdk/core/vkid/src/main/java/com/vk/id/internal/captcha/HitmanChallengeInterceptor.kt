@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.captcha

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.http.HttpResponse
import com.vk.id.network.http.Interceptor

internal class HitmanChallengeInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): HttpResponse {
        val request = chain.request()
        val challengeSolution = request.headers["X-Challenge-Solution"]

        return if (challengeSolution.isNullOrBlank()) {
            HttpResponse(
                request = request,
                code = @Suppress("MagicNumber") 200,
                message = "OK",
                body = "",
                headers = mapOf(
                    "Content-Type" to "application/json; charset=utf-8",
                    "X-Challenge" to "required",
                    "X-Challenge-Url" to "/challenge.html"
                ),
                isRequestSuccessful = true
            )
        } else {
            chain.proceed(request)
        }
    }
}
