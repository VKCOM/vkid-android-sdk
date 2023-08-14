package com.vk.id.internal.auth.web

internal interface BrowserMatcher {
    fun matches(descriptor: BrowserDescriptor): Boolean
}