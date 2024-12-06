package com.vk.id.tracking.tracer

import com.vk.id.common.InternalVKIDApi
import ru.ok.tracer.lite.TracerLite
import ru.ok.tracer.lite.crash.report.TracerCrashReportLite

@InternalVKIDApi
public class TracerCrashReporter(
    tracer: TracerLite
) : CrashReporter {

    private val crashReporter = TracerCrashReportLite(tracer)

    override fun report(crash: Throwable) {
        crashReporter.report(crash)
    }
}
