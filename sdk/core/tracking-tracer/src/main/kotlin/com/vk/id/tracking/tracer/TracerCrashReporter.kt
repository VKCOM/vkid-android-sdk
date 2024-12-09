@file:OptIn(InternalVKIDApi::class)

package com.vk.id.tracking.tracer

import com.vk.id.common.InternalVKIDApi
import com.vk.id.tracking.core.CrashReporter
import ru.ok.tracer.lite.TracerLite
import ru.ok.tracer.lite.crash.report.TracerCrashReportLite

internal class TracerCrashReporter(
    tracer: TracerLite
) : CrashReporter {

    private val crashReporter = TracerCrashReportLite(
        tracer,
        TracerCrashReportLite.Configuration.Builder()
            .apply { obfuscatedNonFatalsEnabled = true }
            .build()
    )

    override fun report(crash: Throwable) {
        crashReporter.report(crash)
    }

    override fun <T> runReportingCrashes(
        errorValueProvider: (error: Throwable) -> T,
        action: () -> T
    ): T {
        try {
            return action()
        } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
            try {
                crashReporter.report(t)
            } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
                throw FailedTracerReportingException(t)
            }
            return errorValueProvider(t)
        }
    }

    override suspend fun <T> runReportingCrashesSuspend(
        errorValueProvider: suspend (error: Throwable) -> T,
        action: suspend () -> T
    ): T {
        try {
            return action()
        } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
            try {
                crashReporter.report(t)
            } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
                throw FailedTracerReportingException(t)
            }
            return errorValueProvider(t)
        }
    }
}
