@file:OptIn(InternalVKIDApi::class)

package com.vk.id.tracking.tracer

import com.vk.id.common.InternalVKIDApi
import com.vk.id.tracking.core.AnalyticsTracking
import com.vk.id.tracking.core.CrashReporter
import ru.ok.tracer.lite.TracerLite
import ru.ok.tracer.lite.crash.report.TracerCrashReportLite

internal class TracerCrashReporter(
    tracer: TracerLite?
) : CrashReporter, AnalyticsTracking {

    private val crashReporter = try {
        tracer?.let {
            TracerCrashReportLite(
                it,
                TracerCrashReportLite.Configuration.Builder()
                    .apply { obfuscatedNonFatalsEnabled = true }
                    .build()
            )
        }
    } catch (@Suppress("TooGenericExceptionCaught") _: Throwable) {
        null
    }

    override fun log(message: String) {
        try {
            crashReporter?.log(message)
        } catch (@Suppress("TooGenericExceptionCaught") _: Throwable) {
        }
    }

    override fun report(crash: Throwable) {
        try {
            crashReporter?.report(crash)
        } catch (@Suppress("TooGenericExceptionCaught") _: Throwable) {
        }
    }

    override fun <T> runReportingCrashes(
        errorValueProvider: (error: Throwable) -> T,
        action: () -> T
    ): T {
        try {
            return action()
        } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
            report(t)
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
            report(t)
            return errorValueProvider(t)
        }
    }
}
