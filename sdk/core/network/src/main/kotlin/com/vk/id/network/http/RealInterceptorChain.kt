@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

/**
 * Реализация цепочки интерсепторов.
 * Управляет последовательностью вызовов интерсепторов и обеспечивает поддержку
 * вложенных вызовов proceed() для retry, аутентификации и других сценариев.
 *
 * @param interceptors Список интерсепторов в цепочке
 * @param index Индекс текущего интерсептора в цепочке
 * @param request Текущий запрос
 * @param httpClient HTTP клиент для выполнения фактического запроса
 */
internal class RealInterceptorChain(
    private val interceptors: List<Interceptor>,
    private val index: Int,
    private val request: HttpRequest,
    private val httpClient: HttpClient
) : Interceptor.Chain {

    private val calls = AtomicInteger()

    override fun request(): HttpRequest = request

    override suspend fun proceed(request: HttpRequest): HttpResponse {
        if (calls.get() >= CALLS_LIMIT) {
            throw IOException("Interceptor chain calls limit ($CALLS_LIMIT) exceeded")
        }

        calls.incrementAndGet()

        val next = RealInterceptorChain(
            interceptors = interceptors,
            index = index + 1,
            request = request,
            httpClient = httpClient
        )

        return if (index < interceptors.size) {
            interceptors[index].intercept(next)
        } else {
            executeRequest(request)
        }
    }

    override fun index(): Int = index

    override fun size(): Int = interceptors.size

    /**
     * Выполняет фактический HTTP запрос.
     * Вызывается когда все интерсепторы в цепочке пройдены.
     */
    @Throws(IOException::class)
    internal suspend fun executeRequest(request: HttpRequest = this.request): HttpResponse {
        currentCoroutineContext().ensureActive()
        val httpResponse = withContext(Dispatchers.IO) {
            httpClient.executeWithInterceptors(request)
        }

        return HttpResponse(
            request = request,
            code = httpResponse.code,
            message = httpResponse.message,
            body = httpResponse.body,
            headers = httpResponse.headers,
            isRequestSuccessful = httpResponse.code in 200..299
        )
    }

    private companion object {
        const val CALLS_LIMIT = 100
    }
}
