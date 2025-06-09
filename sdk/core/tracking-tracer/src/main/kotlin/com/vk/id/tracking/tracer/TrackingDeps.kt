package com.vk.id.tracking.tracer

import android.content.Context
import com.vk.id.common.InternalVKIDApi
import com.vk.id.tracking.core.AnalyticsTracking
import com.vk.id.tracking.core.CrashReporter
import com.vk.id.tracking.core.PerformanceTracker
import ru.ok.tracer.lite.TracerLite
import java.util.concurrent.Executors

@InternalVKIDApi
public class TrackingDeps(
    context: Context,
    clientId: String
) {
    private val tracer = try {
        TracerLite(
            context.applicationContext,
            libraryPackageName = "com.vk.id.tracking.tracer",
            configuration = TracerLite.Configuration.build {
                ioExecutor = Executors.newCachedThreadPool {
                    object : Thread() {
                        override fun run() {
                            try {
                                it.run()
                            } catch (_: Throwable) {
                            }
                        }
                    }
                }
            },
        )
    } catch (@Suppress("TooGenericExceptionCaught") _: Throwable) {
        null
    }

    init {
        try {
            tracer?.setKey("ClientId", clientId)
        } catch (@Suppress("TooGenericExceptionCaught") _: Throwable) {
        }
    }

    private val crashReporterInternal = TracerCrashReporter(tracer)
    public val crashReporter: CrashReporter = crashReporterInternal
    public val performanceTracker: PerformanceTracker = TracerPerformanceTracker(tracer)
    public val analyticsTracking: AnalyticsTracking = crashReporterInternal
}
