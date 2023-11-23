package com.vk

import com.vk.id.VKID
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

internal class AuthCallbacksHolder {
    private var authCallbacks = Collections.newSetFromMap(ConcurrentHashMap<VKID.AuthCallback, Boolean>())

    fun add(callback: VKID.AuthCallback) {
        authCallbacks.add(callback)
    }

    fun getAll(): Set<VKID.AuthCallback> {
        return authCallbacks
    }
}
