package com.vk.id.tracking.core

import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface CrashReporter {
    public fun report(crash: Throwable)
    public fun <T> runReportingCrashes(
        errorValueProvider: (error: Throwable) -> T,
        action: () -> T
    ): T
    public suspend fun <T> runReportingCrashesSuspend(
        errorValueProvider: suspend (error: Throwable) -> T,
        action: suspend () -> T
    ): T
}
