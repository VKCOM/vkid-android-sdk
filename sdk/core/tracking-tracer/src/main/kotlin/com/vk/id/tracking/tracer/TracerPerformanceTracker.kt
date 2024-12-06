package com.vk.id.tracking.tracer

import android.os.SystemClock
import com.vk.id.common.InternalVKIDApi
import ru.ok.tracer.lite.TracerLite
import ru.ok.tracer.lite.performance.metrics.TracerPerformanceMetricsLite
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@InternalVKIDApi
public class TracerPerformanceTracker internal constructor(
    private val reporter: TracerPerformanceMetricsLite,
    private val systemClockProvider: () -> Long = { SystemClock.elapsedRealtimeNanos() }
) {

    public constructor(
        tracerLite: TracerLite,
    ) : this(TracerPerformanceMetricsLite(tracerLite))

    private val startTimes = ConcurrentHashMap<String, Long>()

    @Synchronized
    public fun startTracking(key: String) {
        startTimes[key] = systemClockProvider()
    }

    @Synchronized
    public fun endTracking(key: String) {
        val startTime = startTimes.remove(key) ?: return
        val trackedTime = systemClockProvider() - startTime
        reporter.sample(
            key,
            trackedTime,
            TimeUnit.NANOSECONDS
        )
    }

    public fun runTracking(key: String, action: () -> Unit) {
        startTracking(key)
        action()
        endTracking(key)
    }

    public suspend fun runTrackingSuspend(key: String, action: suspend () -> Unit) {
        startTracking(key)
        action()
        endTracking(key)
    }
}
