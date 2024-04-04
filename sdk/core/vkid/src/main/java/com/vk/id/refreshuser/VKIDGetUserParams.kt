package com.vk.id.refreshuser

/**
 * Create [VKIDGetUserParams].
 *
 * @param initializer params' initialization.
 */
public inline fun VKIDGetUserParams(
    initializer: VKIDGetUserParams.Builder.() -> Unit,
): VKIDGetUserParams {
    return VKIDGetUserParams.Builder().apply(initializer).build()
}

/**
 * Encapsulated parameters for user data receiving.
 */
public class VKIDGetUserParams private constructor(
    internal val state: String? = null,
    internal val refreshTokenState: String? = null,
) {
    /**
     * A builder for [VKIDGetUserParams].
     */
    public class Builder {

        /**
         * An optional state to be passed to user data fetching.
         */
        public var state: String? = null

        /**
         * An optional state to be passed to token refreshing.
         * This can be needed because access token might be refreshed during user data access.
         * The token might be refreshed not more then once and this state will be passes to refreshing method.
         */
        public var refreshTokenState: String? = null

        /**
         * Constructs [VKIDGetUserParams] with provided values.
         */
        public fun build(): VKIDGetUserParams = VKIDGetUserParams(
            state = state,
            refreshTokenState = refreshTokenState,
        )
    }
}
