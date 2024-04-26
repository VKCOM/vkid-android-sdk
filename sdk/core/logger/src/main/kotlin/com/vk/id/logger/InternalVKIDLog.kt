package com.vk.id.logger

import com.vk.id.common.InternalVKIDApi

/**
 * Global singleton class to manage logging.
 *
 * How to use:
 * create logger for your class: val logger = VKIDLog.createLoggerForTag("tag")
 * and use: logger.debug("debug")
 *
 * Nothing is logged by default, to start actual logging you need to set the LogEngine with [setLogEngine].
 */
@InternalVKIDApi
public object InternalVKIDLog : LogEngine {
    private var logEngine: LogEngine = InternalVKIDFakeLogEngine()

    @InternalVKIDApi
    public fun setLogEngine(logEngine: LogEngine) {
        InternalVKIDLog.logEngine = logEngine
    }

    @InternalVKIDApi
    public fun createLoggerForTag(tag: String): InternalVKIDLogger {
        return InternalVKIDLoggerWithTag(tag, this)
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

@InternalVKIDApi
public inline fun <reified T> T.internalVKIDCreateLoggerForClass(): InternalVKIDLogger {
    return InternalVKIDLog.createLoggerForTag(T::class.java.simpleName)
}
