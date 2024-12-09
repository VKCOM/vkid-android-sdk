package com.vk.id.tracking.tracer

import android.content.Context
import com.vk.id.common.InternalVKIDApi
import com.vk.id.tracking.core.CrashReporter
import com.vk.id.tracking.core.PerformanceTracker

@Suppress("UNUSED_PARAMETER")
@InternalVKIDApi
public class TrackingDeps(
    context: Context,
    clientId: String,
) {
    public val crashReporter: CrashReporter = object : CrashReporter {
        override fun report(crash: Throwable) = Unit
        override fun <T> runReportingCrashes(errorValueProvider: (error: Throwable) -> T, action: () -> T): T {
            return action()
        }

        override suspend fun <T> runReportingCrashesSuspend(errorValueProvider: suspend (error: Throwable) -> T, action: suspend () -> T): T {
            return action()
        }
    }
    public val performanceTracker: PerformanceTracker = object : PerformanceTracker {
        override fun startTracking(key: String) = Unit
        override fun endTracking(key: String) = Unit
        override fun runTracking(key: String, action: () -> Unit) = Unit
        override suspend fun runTrackingSuspend(key: String, action: suspend () -> Unit) = Unit
    }
}
