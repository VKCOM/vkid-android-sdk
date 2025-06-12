package com.vk.id.exchangetoken

/**
 * Create [VKIDExchangeTokenParams].
 *
 * @param initializer params' initialization.
 *
 * @since 2.0.0-alpha
 */
public inline fun VKIDExchangeTokenParams(
    initializer: VKIDExchangeTokenParams.Builder.() -> Unit,
): VKIDExchangeTokenParams {
    return VKIDExchangeTokenParams.Builder().apply(initializer).build()
}

/**
 * Encapsulated parameters for exchanging v1 token to v2.
 *
 * @since 2.0.0-alpha
 */
public class VKIDExchangeTokenParams private constructor(
    internal val state: String? = null,
    internal val codeExchangeState: String? = null,
    internal val codeChallenge: String? = null,
) {
    /**
     * A builder for [VKIDExchangeTokenParams].
     *
     * @since 2.0.0-alpha
     */
    public class Builder {

        /**
         * An optional state to be passed to auth.
         *
         * @since 2.0.0-alpha
         */
        public var state: String? = null

        /**
         * An optional state to be passed to exchanging code for access token.
         *
         * @since 2.0.0-alpha
         */
        public var codeExchangeState: String? = null

        /**
         * An optional code challenge to be passed to auth.
         * See https://datatracker.ietf.org/doc/html/rfc7636#section-4.2 for more information.
         *
         * @since 2.0.0-alpha
         */
        public var codeChallenge: String? = null

        /**
         * Constructs [VKIDExchangeTokenParams] with provided values.
         *
         * @since 2.0.0-alpha
         */
        public fun build(): VKIDExchangeTokenParams = VKIDExchangeTokenParams(
            state = state,
            codeExchangeState = codeExchangeState,
            codeChallenge = codeChallenge,
        )
    }
}
