@file:OptIn(InternalVKIDApi::class)

package com.vk.id.tracking.tracer

import android.os.SystemClock
import com.vk.id.common.InternalVKIDApi
import com.vk.id.tracking.core.PerformanceTracker
import ru.ok.tracer.lite.TracerLite
import ru.ok.tracer.lite.performance.metrics.TracerPerformanceMetricsLite
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

internal class TracerPerformanceTracker(
    private val reporter: TracerPerformanceMetricsLite?,
    private val systemClockProvider: () -> Long = { SystemClock.elapsedRealtimeNanos() }
) : PerformanceTracker {

    constructor(
        tracerLite: TracerLite?,
    ) : this(
        try {
            tracerLite?.let(::TracerPerformanceMetricsLite)
        } catch (@Suppress("TooGenericExceptionCaught") _: Throwable) {
            null
        }
    )

    private val startTimes = ConcurrentHashMap<String, Long>()

    @Synchronized
    override fun startTracking(key: String) {
        startTimes[key] = systemClockProvider()
    }

    @Synchronized
    override fun endTracking(key: String) {
        val startTime = startTimes.remove(key) ?: return
        val trackedTime = systemClockProvider() - startTime
        try {
            reporter?.sample(
                key,
                trackedTime,
                TimeUnit.NANOSECONDS
            )
        } catch (@Suppress("TooGenericExceptionCaught") _: Throwable) {
        }
    }

    override fun runTracking(key: String, action: () -> Unit) {
        startTracking(key)
        action()
        endTracking(key)
    }

    override suspend fun runTrackingSuspend(key: String, action: suspend () -> Unit) {
        startTracking(key)
        action()
        endTracking(key)
    }
}
