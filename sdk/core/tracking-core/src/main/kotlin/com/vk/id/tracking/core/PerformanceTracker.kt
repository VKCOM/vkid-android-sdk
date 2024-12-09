package com.vk.id.tracking.core

import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface PerformanceTracker {
    public fun startTracking(key: String)
    public fun endTracking(key: String)
    public fun runTracking(key: String, action: () -> Unit)
    public suspend fun runTrackingSuspend(key: String, action: suspend () -> Unit)
}
