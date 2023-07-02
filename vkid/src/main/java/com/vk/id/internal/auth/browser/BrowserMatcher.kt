package com.vk.id.internal.auth.browser

internal interface BrowserMatcher {
    fun matches(descriptor: BrowserDescriptor): Boolean
}