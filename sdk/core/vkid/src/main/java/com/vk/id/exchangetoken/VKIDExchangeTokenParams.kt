package com.vk.id.exchangetoken

/**
 * Create [VKIDExchangeTokenParams].
 *
 * @param initializer params' initialization.
 */
public inline fun VKIDExchangeTokenParams(
    initializer: VKIDExchangeTokenParams.Builder.() -> Unit,
): VKIDExchangeTokenParams {
    return VKIDExchangeTokenParams.Builder().apply(initializer).build()
}

/**
 * Encapsulated parameters for exchanging v1 token to v2.
 */
public class VKIDExchangeTokenParams private constructor(
    internal val state: String? = null,
) {
    /**
     * A builder for [VKIDExchangeTokenParams].
     */
    public class Builder {

        /**
         * An optional state to be passed to auth.
         */
        public var state: String? = null

        /**
         * Constructs [VKIDExchangeTokenParams] with provided values.
         */
        public fun build(): VKIDExchangeTokenParams = VKIDExchangeTokenParams(
            state = state,
        )
    }
}
