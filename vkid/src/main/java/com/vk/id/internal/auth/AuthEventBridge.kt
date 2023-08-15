package com.vk.id.internal.auth

internal object AuthEventBridge {
    internal var listener: Listener? = null

    internal fun onAuthResult(authResult: AuthResult) {
        listener?.onAuthResult(authResult)
    }

    internal interface Listener {
        fun onAuthResult(authResult: AuthResult)
    }
}