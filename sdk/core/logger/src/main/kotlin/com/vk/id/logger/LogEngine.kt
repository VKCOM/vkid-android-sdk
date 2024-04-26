package com.vk.id.logger

/**
 * Interface defining a logging engine.
 * Implement this interface to create a concrete logging platform.
 * For an example, see [InternalVKIDAndroidLogcatLogEngine].
 *
 * @property LogLevel The log level (INFO, DEBUG, ERROR).
 * @function log Logs a message with a specified log level, tag, message, and optional throwable.
 */
public interface LogEngine {
    /**
     * Represents a log level for logging messages.
     */
    public enum class LogLevel {
        /**
         * Represents info log level.
         */
        INFO,

        /**
         * Represents debug log level.
         */
        DEBUG,

        /**
         * Represents error log level.
         */
        ERROR
    }

    /**
     * Logs [message] and [throwable] with [tag] at [logLevel].
     */
    public fun log(logLevel: LogLevel, tag: String, message: String, throwable: Throwable?)
}
