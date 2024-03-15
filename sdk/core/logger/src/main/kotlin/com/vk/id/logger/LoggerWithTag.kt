package com.vk.id.logger

import com.vk.id.common.InternalVKIDApi

/**
 * Implementation of [Logger] that remembers tag and proxies calls to [LogEngine] with it.
 */
@InternalVKIDApi
internal class LoggerWithTag(private val tag: String, private val logEngine: LogEngine) : Logger {
    override fun info(message: String) {
        logEngine.log(LogEngine.LogLevel.INFO, tag, message, null)
    }

    override fun debug(message: String) {
        logEngine.log(LogEngine.LogLevel.DEBUG, tag, message, null)
    }

    override fun error(message: String, throwable: Throwable?) {
        logEngine.log(LogEngine.LogLevel.ERROR, tag, message, throwable)
    }
}
