package com.vk.id.internal.auth

internal object AuthEventBridge {
    internal var listener: Listener? = null
    internal var authResult: ExternalAuthResult? = null
        set(value) {
            listener?.success(value!!)
            field = value
        }

    internal fun canceled() {
        listener?.canceled()
    }

    internal fun error(message: String, e: Throwable?) {
        listener?.error(message, e)
    }

    internal interface Listener {
        fun success(oauth: ExternalAuthResult)
        fun error(message: String, e: Throwable?)
        fun canceled()
    }
}