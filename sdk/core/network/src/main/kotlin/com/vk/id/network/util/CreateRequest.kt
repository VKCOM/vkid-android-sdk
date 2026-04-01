@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.util

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.InternalVKIDCall
import com.vk.id.network.http.FormBody
import com.vk.id.network.http.HttpClient
import com.vk.id.network.http.HttpRequest
import com.vk.id.network.http.HttpResponse
import com.vk.id.network.http.RealInternalVKIDCall
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Creates an HTTP request for the given host, path, and form body.
 * Returns InternalVKIDCall that defers execution until execute() is called.
 *
 * @param host The host URL (e.g., "https://api.vk.ru")
 * @param path The API path (e.g., "oauth2/auth")
 * @param requestBody The form body to send with the request
 * @param query Optional query parameters
 * @return InternalVKIDCall that can be executed later
 */
@Throws(IOException::class)
internal fun HttpClient.createRequest(
    host: String,
    path: String,
    requestBody: FormBody,
    query: Map<String, String> = emptyMap(),
): InternalVKIDCall<HttpResponse> {
    val url = createUrl(host, path, query)
    return RealInternalVKIDCall(
        httpClient = this,
        request = HttpRequest.post(
            url = url,
            body = requestBody
        ),
        responseMapper = { it }
    )
}

internal fun createUrl(
    host: String,
    path: String,
    query: Map<String, String> = emptyMap()
): String {
    val urlBuilder = StringBuilder(host)

    val normalizedPath = path.trim('/').let {
        if (it.isEmpty()) "" else "/$it"
    }
    urlBuilder.append(normalizedPath)

    if (query.isNotEmpty()) {
        urlBuilder.append("?")
        val queryString = query.entries.joinToString("&") { (name, value) ->
            "${urlEncode(name)}=${urlEncode(value)}"
        }
        urlBuilder.append(queryString)
    }

    return urlBuilder.toString()
}

private fun urlEncode(value: String): String {
    return URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
}
