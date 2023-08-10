package com.vk.id.internal.auth

internal object AuthEventBridge {
    internal var listener: Listener? = null

    internal fun onAuthResult(authResult: AuthResult) {
        listener?.success(authResult)
    }

    internal fun canceled() {
        listener?.canceled()
    }

    internal fun error(message: String, e: Throwable?) {
        listener?.error(message, e)
    }

    internal interface Listener {
        fun success(oauth: AuthResult)
        fun error(message: String, e: Throwable?)
        fun canceled()
    }
}