@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.InternalVKIDLog
import com.vk.id.network.BuildConfig

internal class LoggingInterceptor(tag: String) : Interceptor {

    private val httpLogger = InternalVKIDLog.createLoggerForTag(tag)

    override suspend fun intercept(chain: Interceptor.Chain): HttpResponse {
        if (!BuildConfig.DEBUG) {
            return chain.proceed(chain.request())
        }

        val startTime = System.currentTimeMillis()
        val request = chain.request()

        logRequest(request)

        val response = chain.proceed(request)

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        logResponse(response, duration)

        return response
    }

    private fun logRequest(request: HttpRequest) {
        val logBuilder = StringBuilder().apply {
            append("--> ${request.method} ${request.url}")
            append(System.lineSeparator())

            request.headers.forEach { (key, value) ->
                append("$key: $value")
                append(System.lineSeparator())
            }

            request.body?.let { body ->
                val bodyToLog = if (body.length > MAX_LOG_BODY_LENGTH) {
                    body.take(MAX_LOG_BODY_LENGTH) + "... (truncated)"
                } else {
                    body
                }
                append(System.lineSeparator())
                append(bodyToLog)
                append(System.lineSeparator())
            }

            append("--> END ${request.method}")
        }

        httpLogger.debug(logBuilder.toString())
    }

    private fun logResponse(response: HttpResponse, duration: Long) {
        val logBuilder = StringBuilder().apply {
            append("<-- ${response.code} ${response.message} ${response.request.url} (${duration}ms)")
            append(System.lineSeparator())

            response.headers.forEach { (key, value) ->
                append("$key: $value")
                append(System.lineSeparator())
            }

            response.body?.let { body ->
                val bodyToLog = if (body.length > MAX_LOG_BODY_LENGTH) {
                    body.take(MAX_LOG_BODY_LENGTH) + "... (truncated)"
                } else {
                    body
                }
                append(System.lineSeparator())
                append(bodyToLog)
                append(System.lineSeparator())
            }

            append("<-- END HTTP")
        }

        httpLogger.debug(logBuilder.toString())
    }

    private companion object {
        const val MAX_LOG_BODY_LENGTH = 1024
    }
}
