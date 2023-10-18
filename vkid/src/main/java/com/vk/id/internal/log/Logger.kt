package com.vk.id.internal.log

/**
 * Simple logging
 */
internal interface Logger {
    fun info(message: String)
    fun debug(message: String)
    fun error(message: String, throwable: Throwable?)
}
