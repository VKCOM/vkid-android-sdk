@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi

/**
 * @param request Исходный запрос
 * @param code HTTP статус код
 * @param message Сообщение статуса
 * @param body Тело ответа
 * @param headers Заголовки ответа
 * @param isRequestSuccessful Успешен ли запрос (2xx)
 */
@InternalVKIDApi
public class HttpResponse(
    @InternalVKIDApi
    public val request: HttpRequest,
    @InternalVKIDApi
    public val code: Int,
    @InternalVKIDApi
    public val message: String?,
    @InternalVKIDApi
    public val body: String?,
    @InternalVKIDApi
    public val headers: Map<String, String> = emptyMap(),
    @InternalVKIDApi
    public val isRequestSuccessful: Boolean = false,
) {
    public val isSuccessful: Boolean get() = code in HTTP_OK..HTTP_MULTIPLE_CHOICES

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpResponse) return false
        return request == other.request &&
            code == other.code &&
            message == other.message &&
            body == other.body &&
            headers == other.headers &&
            isRequestSuccessful == other.isRequestSuccessful
    }

    override fun hashCode(): Int {
        var result = request.hashCode()
        result = 31 * result + code
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + headers.hashCode()
        result = 31 * result + isRequestSuccessful.hashCode()
        return result
    }

    override fun toString(): String {
        return "HttpResponse(request=$request, code=$code, message=$message, body=$body, headers=$headers, isRequestSuccessful=$isRequestSuccessful)"
    }

    public val isRedirect: Boolean get() = code in REDIRECT_CODES

    public val isClientError: Boolean get() = code in CLIENT_ERROR_CODES

    public val isServerError: Boolean get() = code in SERVER_ERROR_CODES

    /**
     * Получает тело ответа как строку (аналог peekBody в OkHttp).
     *
     * @param maxBytes Максимальное количество байт для чтения
     * @return Тело ответа или null, если тело отсутствует
     */
    @InternalVKIDApi
    public fun peekBody(maxBytes: Long): ResponseBody? {
        val actualBody = body ?: return null
        val bytesToRead = minOf(maxBytes, actualBody.length.toLong())
        return ResponseBody(
            string = actualBody.take(bytesToRead.toInt()),
            contentType = headers["Content-Type"]
        )
    }

    public fun headerValues(name: String): List<String> {
        val value = headers[name]
        return if (value != null) listOf(value) else emptyList()
    }

    public fun newBuilder(): Builder = Builder(this)

    /**
     * Builder для создания и модификации Response.
     */
    @InternalVKIDApi
    public class Builder {
        private var request: HttpRequest
        private var code: Int
        private var message: String?
        private var body: String?
        private var headers: MutableMap<String, String>
        private var isRequestSuccessful: Boolean

        internal constructor(response: HttpResponse) {
            this.request = response.request
            this.code = response.code
            this.message = response.message
            this.body = response.body
            this.headers = response.headers.toMutableMap()
            this.isRequestSuccessful = response.isRequestSuccessful
        }

        public fun request(request: HttpRequest): Builder = apply { this.request = request }

        public fun code(code: Int): Builder = apply { this.code = code }

        public fun message(message: String?): Builder = apply { this.message = message }

        public fun body(body: String?): Builder = apply { this.body = body }

        public fun header(name: String, value: String): Builder = apply { this.headers[name] = value }

        public fun headers(headers: Map<String, String>): Builder = apply {
            this.headers.clear()
            this.headers.putAll(headers)
        }

        public fun isRequestSuccessful(isSuccessful: Boolean): Builder = apply {
            this.isRequestSuccessful = isSuccessful
        }

        public fun build(): HttpResponse = HttpResponse(
            request = request,
            code = code,
            message = message,
            body = body,
            headers = headers.toMap(),
            isRequestSuccessful = isRequestSuccessful
        )
    }

    private companion object {
        private const val HTTP_OK = 200
        private const val HTTP_MULTIPLE_CHOICES = 300

        val REDIRECT_CODES = 300..399
        val CLIENT_ERROR_CODES = 400..499
        val SERVER_ERROR_CODES = 500..599
    }
}

/**
 * Представление тела ответа для метода peekBody().
 *
 * @property string Тело ответа как строка
 * @property contentType Content-Type
 */
@InternalVKIDApi
public class ResponseBody(
    @InternalVKIDApi
    public val string: String,
    @InternalVKIDApi
    public val contentType: String?
) {
    public val length: Int get() = string.length

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ResponseBody) return false
        return string == other.string && contentType == other.contentType
    }

    override fun hashCode(): Int {
        var result = string.hashCode()
        result = 31 * result + (contentType?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ResponseBody(string='$string', contentType=$contentType)"
    }
}
