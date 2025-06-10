package com.vk.id.logger

/**
 * Interface defining a logging engine.
 * Implement this interface to create a concrete logging platform.
 * For an example, see [InternalVKIDAndroidLogcatLogEngine].
 *
 * @property LogLevel The log level (INFO, DEBUG, ERROR).
 * @function log Logs a message with a specified log level, tag, message, and optional throwable.
 *
 * @since 0.0.1
 */
public interface LogEngine {
    /**
     * Represents a log level for logging messages.
     *
     * @since 0.0.1
     */
    public enum class LogLevel {
        /**
         * Represents info log level.
         *
         * @since 0.0.1
         */
        INFO,

        /**
         * Represents debug log level.
         *
         * @since 0.0.1
         */
        DEBUG,

        /**
         * Represents error log level.
         *
         * @since 0.0.1
         */
        ERROR
    }

    /**
     * Logs [message] and [throwable] with [tag] at [logLevel].
     *
     * @since 0.0.1
     */
    public fun log(logLevel: LogLevel, tag: String, message: String, throwable: Throwable?)
}
