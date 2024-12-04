package com.vk.id.tracking.tracer

import android.content.Context
import com.vk.id.common.InternalVKIDApi
import ru.ok.tracer.lite.TracerLite
import ru.ok.tracer.lite.crash.report.TracerCrashReportLite

@InternalVKIDApi
public class TracerCrashReporter(
    context: Context
) : CrashReporter {

    private val tracer = TracerLite(
        context.applicationContext,
        libraryPackageName = "com.vk.id",
    )

    private val crashReporter = TracerCrashReportLite(tracer)

    override fun report(crash: Throwable) {
        crashReporter.report(crash)
    }
}
