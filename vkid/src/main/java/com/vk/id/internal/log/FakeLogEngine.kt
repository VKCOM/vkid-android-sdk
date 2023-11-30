package com.vk.id.internal.log

/**
 * [LogEngine] implementation that doesn't perform any logging.
 */
internal class FakeLogEngine : LogEngine {
    override fun log(
        logLevel: LogEngine.LogLevel,
        tag: String,
        message: String,
        throwable: Throwable?
    ) {
        // nothing
    }
}
