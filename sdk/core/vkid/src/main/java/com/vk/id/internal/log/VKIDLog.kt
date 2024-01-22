package com.vk.id.internal.log

/**
 * Global singleton class to manage logging.
 *
 * How to use:
 * create logger for your class: val logger = VKIDLog.createLoggerForTag("tag")
 * and use: logger.debug("debug")
 *
 * Nothing is logged by default, to start actual logging you need to set the LogEngine with [setLogEngine].
 */
internal object VKIDLog : LogEngine {
    private var logEngine: LogEngine = FakeLogEngine()

    internal fun setLogEngine(logEngine: LogEngine) {
        this.logEngine = logEngine
    }

    internal fun createLoggerForTag(tag: String): Logger {
        return LoggerWithTag(tag, this)
    }

    override fun log(
        logLevel: LogEngine.LogLevel,
        tag: String,
        message: String,
        throwable: Throwable?
    ) {
        logEngine.log(logLevel, tag, message, throwable)
    }
}

internal inline fun <reified T> T.createLoggerForClass(): Logger {
    return VKIDLog.createLoggerForTag(T::class.java.simpleName)
}
