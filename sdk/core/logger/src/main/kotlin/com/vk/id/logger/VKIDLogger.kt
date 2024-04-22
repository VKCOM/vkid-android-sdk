package com.vk.id.logger

import com.vk.id.common.InternalVKIDApi

/**
 * Simple logging
 */
@InternalVKIDApi
public interface VKIDLogger {
    public fun info(message: String)
    public fun debug(message: String)
    public fun error(message: String, throwable: Throwable?)
}
