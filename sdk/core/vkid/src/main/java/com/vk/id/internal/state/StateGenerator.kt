@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.state

import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.store.InternalVKIDPrefsStore

internal class StateGenerator(
    private val prefsStore: InternalVKIDPrefsStore
) {

    companion object {
        private const val STATE_LENGTH = 32
    }
    fun regenerateState(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val state = (1..STATE_LENGTH).map { allowedChars.random() }.joinToString("")
        prefsStore.state = state
        return state
    }
}
