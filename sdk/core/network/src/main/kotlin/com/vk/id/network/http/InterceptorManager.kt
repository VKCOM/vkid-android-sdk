@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.InternalVKIDLog
import java.io.IOException

internal class InterceptorManager(
    private val httpClient: HttpClient,
    applicationInterceptors: List<Interceptor> = emptyList(),
) {
    private val logger = InternalVKIDLog.createLoggerForTag("InterceptorManager")

    private val applicationInterceptors: List<Interceptor> = applicationInterceptors.toList()

    @Throws(IOException::class)
    public suspend fun proceed(request: HttpRequest): HttpResponse {
        // Создаём цепочку с application интерсепторами
        val allInterceptors = applicationInterceptors + HttpExecutionInterceptor(httpClient)

        val chain = RealInterceptorChain(
            interceptors = allInterceptors,
            index = 0,
            request = request,
            httpClient = httpClient
        )

        return chain.proceed(request)
    }

    public fun addApplicationInterceptor(interceptor: Interceptor) {
        applicationInterceptors.toMutableList().add(interceptor)
        logger.debug("Added application interceptor: ${interceptor.javaClass.simpleName}")
    }

    /**
     * Внутренний интерсептор для выполнения фактического HTTP запроса.
     * Это последний интерсептор в цепочке.
     */
    private class HttpExecutionInterceptor(
        private val httpClient: HttpClient
    ) : Interceptor {
        override suspend fun intercept(chain: Interceptor.Chain): HttpResponse {
            return httpClient.executeHttpRequest(chain.request())
        }
    }
}
