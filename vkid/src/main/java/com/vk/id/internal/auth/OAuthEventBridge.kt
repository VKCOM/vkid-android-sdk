package com.vk.id.internal.auth

internal object OAuthEventBridge {
    internal var listener: Listener? = null
    internal var oauthResult: ExternalOauthResult? = null
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
        fun success(oauth: ExternalOauthResult)
        fun error(message: String, e: Throwable?)
        fun canceled()
    }
}