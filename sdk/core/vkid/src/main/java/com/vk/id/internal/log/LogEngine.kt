package com.vk.id.internal.log

/**
 * Interface defining a logging engine.
 * Implement this interface to create a concrete logging platform.
 * For an example, see [AndroidLogcatLogEngine].
 *
 * @property LogLevel The log level (INFO, DEBUG, ERROR).
 * @function log Logs a message with a specified log level, tag, message, and optional throwable.
 */
public interface LogEngine {
    public enum class LogLevel {
        INFO, DEBUG, ERROR
    }

    public fun log(logLevel: LogLevel, tag: String, message: String, throwable: Throwable?)
}
