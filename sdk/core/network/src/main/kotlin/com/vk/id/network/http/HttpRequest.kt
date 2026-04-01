@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi
import java.net.URL
import java.net.URLEncoder

/**
 * @param method HTTP метод (GET, POST, etc.)
 * @param url URL запроса
 * @param body Тело запроса (опционально)
 * @param headers Заголовки запроса (опционально)
 * @param contentType Content-Type (опционально)
 */
@InternalVKIDApi
public class HttpRequest(
    @InternalVKIDApi
    public val method: String,
    @InternalVKIDApi
    public val url: String,
    @InternalVKIDApi
    public val body: String?,
    @InternalVKIDApi
    public val headers: Map<String, String> = emptyMap(),
    @InternalVKIDApi
    public val contentType: String? = null,
) {
    public fun newBuilder(): Builder = Builder(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpRequest) return false
        return method == other.method &&
            url == other.url &&
            body == other.body &&
            headers == other.headers &&
            contentType == other.contentType
    }

    override fun hashCode(): Int {
        var result = method.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + headers.hashCode()
        result = 31 * result + (contentType?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "HttpRequest(method='$method', url='$url', body=$body, headers=$headers, contentType=$contentType)"
    }

    public fun toUrl(): URL = URL(url)

    public fun urlBuilder(): UrlBuilder = UrlBuilder(url)

    public fun header(name: String): String? = headers[name]

    @InternalVKIDApi
    public class Builder {
        private var method: String
        private var url: String
        private var body: String?
        private var headers: MutableMap<String, String>
        private var contentType: String?

        internal constructor(request: HttpRequest) {
            this.method = request.method
            this.url = request.url
            this.body = request.body
            this.headers = request.headers.toMutableMap()
            this.contentType = request.contentType
        }

        @InternalVKIDApi
        public fun method(method: String): Builder = apply { this.method = method }

        public fun url(url: String): Builder = apply { this.url = url }

        public fun body(body: String?): Builder = apply { this.body = body }

        public fun header(name: String, value: String): Builder = apply { this.headers[name] = value }

        public fun addHeader(name: String, value: String): Builder = apply {
            this.headers[name] = value
        }

        public fun headers(headers: Map<String, String>): Builder = apply {
            this.headers.clear()
            this.headers.putAll(headers)
        }

        public fun contentType(contentType: String?): Builder = apply { this.contentType = contentType }

        public fun build(): HttpRequest = HttpRequest(
            method = method,
            url = url,
            body = body,
            headers = headers.toMap(),
            contentType = contentType
        )
    }

    @InternalVKIDApi
    public companion object {
        public fun get(url: String): HttpRequest = HttpRequest(
            method = "GET",
            url = url,
            body = null
        )

        public fun post(url: String, body: String?): HttpRequest = HttpRequest(
            method = "POST",
            url = url,
            body = body,
            contentType = ""
        )

        public fun post(url: String, body: FormBody): HttpRequest = HttpRequest(
            method = "POST",
            url = url,
            body = body.content(),
            contentType = "application/x-www-form-urlencoded; charset=UTF-8"
        )
    }
}

@InternalVKIDApi
public class UrlBuilder(private val originalUrl: String) {

    private val url = URL(originalUrl)
    private val queryParams: MutableMap<String, String> = mutableMapOf()

    public fun addQueryParameter(name: String, value: String): UrlBuilder = apply {
        queryParams[name] = URLEncoder.encode(value, "UTF-8")
    }

    public fun build(): String {
        val queryString = if (queryParams.isNotEmpty()) {
            val existingQuery = url.query
            val newQuery = queryParams.entries.joinToString("&") { (k, v) -> "$k=$v" }
            if (existingQuery.isNullOrEmpty()) {
                "?$newQuery"
            } else {
                "?$existingQuery&$newQuery"
            }
        } else {
            url.query?.let { "?$it" } ?: ""
        }

        return "${url.protocol}://${url.host}$queryString"
    }
}
