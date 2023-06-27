package com.vk.id

import androidx.annotation.WorkerThread

public interface VKIDCall<out T> {
    @WorkerThread
    public fun execute(): Result<T>

    /**
     * Function to cancel [VKIDCall]
     */
    public fun cancel()
}