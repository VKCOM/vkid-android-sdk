package com.vk.id.logger

import com.vk.id.common.InternalVKIDApi

/**
 * [LogEngine] implementation that doesn't perform any logging.
 */
@InternalVKIDApi
public class InternalVKIDFakeLogEngine : LogEngine {
    override fun log(
        logLevel: LogEngine.LogLevel,
        tag: String,
        message: String,
        throwable: Throwable?
    ) {
        // nothing
    }
}
