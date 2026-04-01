@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network

import androidx.annotation.WorkerThread
import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.http.HttpResponse

// Call interface that provides deferred execution, cancellation, and state tracking
// similar to OkHttp Call. Execution is deferred until execute() is called.
@InternalVKIDApi
public interface InternalVKIDCall<out T> {
    /**
     * Executes HTTP request and returns response.
     * This method is suspended and will block until request completes or is cancelled.
     */
    @InternalVKIDApi
    @WorkerThread
    public suspend fun execute(): Result<T>

    /**
     * Cancelss ongoing HTTP request.
     * If the request has not started yet, it will not be executed.
     * If the request is in progress, the underlying HttpURLConnection will be disconnected.
     */
    @InternalVKIDApi
    public fun cancel()

    public fun isCanceled(): Boolean

    public fun isExecuted(): Boolean
}

@InternalVKIDApi
public fun <T> InternalVKIDCall<HttpResponse>.internalVKIDWrapToVKIDCall(
    responseMapping: HttpResponse.() -> T
): InternalVKIDCall<T> {
    return VKIDCallWrapper(delegate = this, responseMapping = responseMapping)
}

private class VKIDCallWrapper<T>(
    private val delegate: InternalVKIDCall<HttpResponse>,
    private val responseMapping: HttpResponse.() -> T
) : InternalVKIDCall<T> {
    override suspend fun execute(): Result<T> {
        return delegate.execute().mapCatching { success: HttpResponse ->
            success.responseMapping()
        }
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isExecuted(): Boolean = delegate.isExecuted()

    override fun isCanceled(): Boolean = delegate.isCanceled()
}
