package com.vk.id.internal.log

/**
 * Interface to implement for concrete logging platform, see [AndroidLogcatLogEngine] for example
 */
internal interface LogEngine {
    enum class LogLevel {
        INFO, DEBUG, ERROR
    }

    fun log(logLevel: LogLevel, tag: String, message: String, throwable: Throwable?)
}
