@file:OptIn(InternalVKIDApi::class)

package com.vk.id.test

import androidx.annotation.WorkerThread
import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.InternalVKIDCall

/**
 * Mock implementation of [InternalVKIDCall] that returns a pre-defined result.
 * Used in UI tests to mock network responses without making actual HTTP requests.
 *
 * @param result The result to return when [execute] is called
 */
internal class MockInternalVKIDCall<out T>(
    private val result: Result<T>
) : InternalVKIDCall<T> {

    /**
     * Returns the pre-defined result immediately without any network call.
     */
    @WorkerThread
    override suspend fun execute(): Result<T> = result

    /**
     * No-op cancellation since there's no actual network call to cancel.
     */
    override fun cancel() {
        // No-op for mock
    }

    /**
     * Always returns false since there's no ongoing network call.
     */
    override fun isCanceled(): Boolean = false

    /**
     * Always returns true since the result is pre-defined.
     */
    override fun isExecuted(): Boolean = true
}
