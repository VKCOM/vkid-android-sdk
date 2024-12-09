package com.vk.id.tracking.tracer

import android.content.Context
import com.vk.id.common.InternalVKIDApi
import com.vk.id.tracking.core.AnalyticsTracking
import com.vk.id.tracking.core.CrashReporter
import com.vk.id.tracking.core.PerformanceTracker
import ru.ok.tracer.lite.TracerLite

@InternalVKIDApi
public class TrackingDeps(
    context: Context,
    clientId: String
) {
    private val tracer = TracerLite(
        context.applicationContext,
        libraryPackageName = "com.vk.id.tracking.tracer",
    )
    init {
        tracer.setKey("ClientId", clientId)
    }
    private val crashReporterInternal = TracerCrashReporter(tracer)
    public val crashReporter: CrashReporter = crashReporterInternal
    public val performanceTracker: PerformanceTracker = TracerPerformanceTracker(tracer)
    public val analyticsTracking: AnalyticsTracking = crashReporterInternal
}
