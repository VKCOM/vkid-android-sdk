package com.vk.id.network.util

import okhttp3.Call
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

internal fun OkHttpClient.createRequest(
    host: String,
    path: String,
    requestBody: RequestBody,
    query: Map<String, String> = emptyMap(),
): Call {
    val url = host.toHttpUrl().newBuilder()
        .apply {
            query.forEach { (name, value) ->
                addQueryParameter(name, value)
            }
        }
        .addPathSegments(path)
        .build()
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()
    return newCall(request)
}
