package com.vk.id.internal.api

import androidx.annotation.WorkerThread

internal interface VKIDCall<out T> {
    @WorkerThread
    fun execute(): Result<T>

    /**
     * Function to cancel [VKIDCall]
     */
    fun cancel()
}
