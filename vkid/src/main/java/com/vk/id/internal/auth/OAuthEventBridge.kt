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

    internal fun error(e: Throwable) {
        listener?.error(e)
    }

    internal interface Listener {
        fun success(oauth: ExternalOauthResult)
        fun error(e: Throwable)
        fun canceled()
    }
}