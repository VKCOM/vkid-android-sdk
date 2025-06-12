package com.vk.id.refreshuser

/**
 * Create [VKIDGetUserParams].
 *
 * @param initializer params' initialization.
 *
 * @since 2.0.0-alpha
 */
public inline fun VKIDGetUserParams(
    initializer: VKIDGetUserParams.Builder.() -> Unit,
): VKIDGetUserParams {
    return VKIDGetUserParams.Builder().apply(initializer).build()
}

/**
 * Encapsulated parameters for user data receiving.
 *
 * @since 2.0.0-alpha
 */
public class VKIDGetUserParams private constructor(
    internal val refreshTokenState: String? = null,
) {
    /**
     * A builder for [VKIDGetUserParams].
     *
     * @since 2.0.0-alpha
     */
    public class Builder {

        /**
         * An optional state to be passed to token refreshing.
         * This can be needed because access token might be refreshed during user data access.
         * The token might be refreshed not more then once and this state will be passes to refreshing method.
         *
         * @since 2.0.0-alpha
         */
        public var refreshTokenState: String? = null

        /**
         * Constructs [VKIDGetUserParams] with provided values.
         *
         * @since 2.0.0-alpha
         */
        public fun build(): VKIDGetUserParams = VKIDGetUserParams(
            refreshTokenState = refreshTokenState,
        )
    }
}
