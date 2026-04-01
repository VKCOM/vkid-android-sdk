@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi

/**
 * Интерсептор для HTTP запросов и ответов.
 * Работает аналогично okhttp3 Interceptor.
 *
 * Интерсепторы могут:
 * - Модифицировать запрос перед отправкой
 * - Модифицировать ответ после получения
 * - Вызывать chain.proceed() несколько раз (для retry, аутентификации)
 * - Прерывать выполнение цепочки
 *
 * Пример использования:
 * ```kotlin
 * class LoggingInterceptor : Interceptor {
 *     override fun intercept(chain: Chain): Response {
 *         val request = chain.request()
 *         println("Sending request to ${request.url}")
 *
 *         val response = chain.proceed(request)
 *
 *         println("Received response: ${response.code}")
 *         return response
 *     }
 * }
 * ```
 */
@InternalVKIDApi
public interface Interceptor {
    /**
     * Перехватывает и обрабатывает запрос.
     *
     * @param chain Цепочка вызовов интерсепторов
     * @return Ответ на запрос
     */
    @InternalVKIDApi
    public suspend fun intercept(chain: Chain): HttpResponse

    /**
     * Цепочка вызовов интерсепторов.
     * Предоставляет доступ к запросу и возможность продолжить выполнение цепочки.
     */
    @InternalVKIDApi
    public interface Chain {
        /**
         * Возвращает текущий запрос.
         */
        @InternalVKIDApi
        public fun request(): HttpRequest

        /**
         * Продолжает выполнение цепочки с указанным запросом.
         * Может вызываться несколько раз для retry, аутентификации и т.д.
         *
         * @param request Запрос для отправки
         * @return Ответ на запрос
         */
        @InternalVKIDApi
        public suspend fun proceed(request: HttpRequest): HttpResponse

        /**
         * Индекс текущего интерсептора в цепочке.
         */
        @InternalVKIDApi
        public fun index(): Int

        /**
         * Общее количество интерсепторов в цепочке.
         */
        @InternalVKIDApi
        public fun size(): Int
    }
}
