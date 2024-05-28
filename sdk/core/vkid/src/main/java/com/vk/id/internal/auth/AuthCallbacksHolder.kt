package com.vk.id.internal.auth

import com.vk.id.auth.VKIDAuthCallback
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

internal class AuthCallbacksHolder {
    private var authCallbacks = Collections.newSetFromMap(ConcurrentHashMap<VKIDAuthCallback, Boolean>())

    fun add(callback: VKIDAuthCallback) {
        authCallbacks.add(callback)
    }

    fun getAll(): Set<VKIDAuthCallback> {
        return authCallbacks
    }

    fun clear() {
        authCallbacks.clear()
    }

    fun isEmpty() = authCallbacks.isEmpty()
}
