package com.vk.id.tracking.tracer

import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public class CrashReportingRunner(
    @Suppress("unused") private val crashReporter: CrashReporter
) {

    public fun <T> runReportingCrashes(
        @Suppress("UNUSED_PARAMETER") errorValueProvider: (error: Throwable) -> T,
        action: () -> T
    ): T {
        return action()
    }

    public suspend fun <T> runReportingCrashesSuspend(
        @Suppress("UNUSED_PARAMETER") errorValueProvider: suspend (error: Throwable) -> T,
        action: suspend () -> T
    ): T {
        return action()
    }
}
