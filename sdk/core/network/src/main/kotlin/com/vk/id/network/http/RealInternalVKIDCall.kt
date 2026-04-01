@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.InternalVKIDLog
import com.vk.id.network.InternalVKIDCall
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.cancellation.CancellationException

internal class RealInternalVKIDCall<T>(
    private val httpClient: HttpClient,
    private val request: HttpRequest,
    private val responseMapper: (HttpResponse) -> T,
) : InternalVKIDCall<T> {

    private var _executed = AtomicBoolean(false)
    private var _cancelled = AtomicBoolean(false)

    private var jobExecution: Job? = null

    private val logger = InternalVKIDLog.createLoggerForTag("RealInternalVKIDCall")

    @Suppress("TooGenericExceptionCaught")
    override suspend fun execute(): Result<T> = withContext(currentCoroutineContext()) {
        synchronized(this@RealInternalVKIDCall) {
            if (_cancelled.get()) {
                return@withContext Result.failure(IOException("Call was cancelled"))
            }
            if (_executed.get()) {
                return@withContext Result.failure(IOException("Call already executed"))
            }
            _executed.set(true)
        }

        val job = async {
            try {
                val response = httpClient.executeWithInterceptors(request)

                Result.success(responseMapper(response))
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                logger.error("Network error during request to ${request.url}", e)
                Result.failure(e)
            } catch (e: Throwable) {
                logger.error("Execution error during request to ${request.url}", e)
                Result.failure(IOException(e.message, e))
            }
        }
        jobExecution = job
        job.await()
    }

    override fun cancel() {
        synchronized(this@RealInternalVKIDCall) {
            if (_cancelled.compareAndSet(false, true)) {
                jobExecution?.cancel()
            }
        }
    }

    override fun isCanceled(): Boolean = synchronized(this@RealInternalVKIDCall) { _cancelled.get() }

    override fun isExecuted(): Boolean = synchronized(this@RealInternalVKIDCall) { _executed.get() }
}
