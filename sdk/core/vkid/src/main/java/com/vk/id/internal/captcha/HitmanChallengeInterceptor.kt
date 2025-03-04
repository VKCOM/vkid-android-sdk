package com.vk.id.internal.captcha

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

internal class HitmanChallengeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (chain.request().header("X-Challenge-Solution").isNullOrBlank()) {
            Response.Builder()
                .code(@Suppress("MagicNumber") 200)
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .message("OK")
                .body("".toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull()))
                .header("X-Challenge", "required")
                .header("X-Challenge-Url", "/challenge.html")
                .build()
        } else {
            chain.proceed(chain.request())
        }
    }
}
