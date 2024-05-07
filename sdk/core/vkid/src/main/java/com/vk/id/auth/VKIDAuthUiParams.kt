package com.vk.id.auth

import com.vk.id.common.InternalVKIDApi

/**
 * Create [VKIDAuthUiParams].
 *
 * @param initializer params' initialization.
 */
public inline fun VKIDAuthUiParams(initializer: VKIDAuthUiParams.Builder.() -> Unit): VKIDAuthUiParams {
    return VKIDAuthUiParams.Builder().apply(initializer).build()
}

/**
 * VKIDAuthUiParams encapsulates ui parameters for VK ID authentication using externally generated auth params.
 *
 * @property state an optional state to be passed to auth.
 * @property codeChallenge an optional code challenge to be passed to auth.
 */
public class VKIDAuthUiParams private constructor(
    internal val state: String?,
    internal val codeChallenge: String?,
    internal val scopes: Set<String>,
) {
    /**
     * Builder for [VKIDAuthUiParams].
     */
    public class Builder {
        /**
         * An optional state to be passed to auth.
         */
        public var state: String? = null

        /**
         * An optional code challenge to be passed to auth.
         * See https://datatracker.ietf.org/doc/html/rfc7636#section-4.2 for more information.
         */
        public var codeChallenge: String? = null

        /**
         * A required parameter with a list of requested scopes for the access token.
         * You can view the list of available scopes here: https://dev.vk.com/ru/reference/access-rights.
         * The user will see a screen where he may grant some of this scopes during authorization process.
         */
        public var scopes: Set<String> = emptySet()

        /**
         * Constructs [VKIDAuthUiParams] object with provided values.
         */
        public fun build(): VKIDAuthUiParams = VKIDAuthUiParams(
            state = state,
            codeChallenge = codeChallenge,
            scopes = scopes,
        )
    }

    @InternalVKIDApi
    public fun newBuilder(initializer: Builder.() -> Unit): VKIDAuthUiParams {
        val params = this
        return VKIDAuthUiParams {
            state = params.state
            codeChallenge = params.codeChallenge
            scopes = params.scopes
            initializer()
        }
    }

    @InternalVKIDApi
    public fun asParamsBuilder(initializer: VKIDAuthParams.Builder.() -> Unit): VKIDAuthParams.Builder {
        val params = this
        return VKIDAuthParams.Builder().apply {
            state = params.state
            codeChallenge = params.codeChallenge
            scopes = params.scopes
            internalUse = true
            initializer()
        }
    }
}
