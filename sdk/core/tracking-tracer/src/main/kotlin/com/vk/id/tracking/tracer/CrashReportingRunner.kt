package com.vk.id.tracking.tracer

import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public class CrashReportingRunner(
    private val crashReporter: CrashReporter
) {

    public fun <T> runReportingCrashes(
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

    public suspend fun <T> runReportingCrashesSuspend(
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
