@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.InternalVKIDLog
import kotlinx.coroutines.delay
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.random.Random

/**
 * Интерцептор для автоматического повтора запросов при временных ошибках сети.
 * Работает в цепочке интерсепторов и поддерживает вложенные вызовы chain.proceed().
 *
 * Повторяет запросы для идемпотентных методов (GET, HEAD, OPTIONS) при ошибке.
 * Использует экспоненциальную задержку между попытками.
 *
 * Пример использования:
 * ```kotlin
 * val retryInterceptor = RetryInterceptor(
 *     maxRetries = 3,
 *     retryableStatusCodes = setOf(408, 429, 500, 502, 503, 504)
 * )
 * ```
 *
 * @property maxRetries Максимальное количество попыток
 * @property retryableStatusCodes Статус коды, при которых нужно повторить запрос
 */
internal class RetryInterceptor(
    public val maxRetries: Int = DEFAULT_MAX_RETRIES,
    private val retryableStatusCodes: Set<Int> = DEFAULT_RETRYABLE_STATUSES
) : Interceptor {

    private val logger = InternalVKIDLog.createLoggerForTag("RetryInterceptor")

    override suspend fun intercept(chain: Interceptor.Chain): HttpResponse {
        var lastException: Throwable? = null
        var lastResponse: HttpResponse? = null

        for (attempt in 0..maxRetries) {
            try {
                val response = chain.proceed(chain.request())

                if (attempt < maxRetries && shouldRetryByStatusCode(response.code)) {
                    logger.debug("Retry attempt $attempt for ${chain.request().url} due to status code: ${response.code}")
                    val delay = getRetryDelay(attempt)
                    if (delay > 0) {
                        delay(delay)
                    }
                    continue
                }

                return response
            } catch (e: IOException) {
                lastException = e

                val shouldRetry = attempt < maxRetries &&
                    isIdempotent(chain.request().method) &&
                    isRetryableError(e)

                if (!shouldRetry) {
                    logger.error("Request failed after $attempt attempt(s): ${e.message}", e)
                    throw e
                }

                logger.debug("Retry attempt $attempt for ${chain.request().url} due to error: ${e.message}")

                val delay = getRetryDelay(attempt)
                if (delay > 0) {
                    delay(delay)
                }
            }
        }

        lastException?.let { throw it }
        lastResponse?.let { return it }
        throw IOException("Request failed after $maxRetries attempts")
    }

    /**
     * Проверяет, следует ли повторить запрос при данном статус коде.
     */
    private fun shouldRetryByStatusCode(statusCode: Int): Boolean {
        return statusCode in retryableStatusCodes
    }

    /**
     * Проверяет, является ли ошибка повторяемой.
     */
    private fun isRetryableError(error: Throwable): Boolean {
        return when (error) {
            is SocketTimeoutException -> true // Таймаут соединения
            is UnknownHostException -> true // Неизвестный хост (временные DNS проблемы)
            is IOException -> {
                // Проверяем сообщение ошибки для различных сетевых проблем
                val message = error.message?.lowercase() ?: return false
                message.contains("connection reset") ||
                    message.contains("connection refused") ||
                    message.contains("network is unreachable") ||
                    message.contains("broken pipe")
            }

            else -> false
        }
    }

    /**
     * Проверяет, является ли метод идемпотентным.
     * Идемпотентные методы безопасны для повторения.
     */
    private fun isIdempotent(method: String): Boolean {
        return method.uppercase() in IDEMPOTENT_METHODS
    }

    /**
     * Вычисляет экспоненциальную задержку между попытками.
     *
     * @param attempt Номер попытки (начиная с 0)
     * @return Задержка в миллисекундах
     */
    public fun getRetryDelay(attempt: Int): Long {
        val attemptFactor = 1 shl attempt
        val randomDelay = Random.nextInt(1, BASE_RETRY_DELAY_MS.div(2).toInt())
        val delay = (BASE_RETRY_DELAY_MS + randomDelay) * attemptFactor
        return delay.coerceAtMost(MAX_RETRY_DELAY_MS)
    }

    private companion object {
        const val DEFAULT_MAX_RETRIES = 3
        const val BASE_RETRY_DELAY_MS = 1000L // Базовая задержка 1 секунда
        const val MAX_RETRY_DELAY_MS = 5000L // Максимальная задержка 5 секунд

        val IDEMPOTENT_METHODS = setOf("GET", "HEAD", "OPTIONS", "PUT", "DELETE")

        // Статусы, при которых можно повторить запрос
        val DEFAULT_RETRYABLE_STATUSES = setOf(
            408, // Request Timeout
            429, // Too Many Requests
            500, // Internal Server Error
            502, // Bad Gateway
            503, // Service Unavailable
            504 // Gateway Timeout
        )
    }
}
