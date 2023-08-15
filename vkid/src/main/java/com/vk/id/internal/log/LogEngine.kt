package com.vk.id.internal.log

/**
 * Interface to implement for concrete logging platform, see [AndroidLogcatLogEngine] for example
 */
public interface LogEngine {
    public enum class LogLevel {
        INFO, DEBUG, ERROR
    }

    public fun log(logLevel: LogLevel, tag: String, message: String, throwable: Throwable?)
}
