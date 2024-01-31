package com.vk.id.commn

/**
 * This annotation marks APIs that you should not use under any circumstances.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is internal VK ID api, do not use it in your code"
)
public annotation class InternalVKIDApi
