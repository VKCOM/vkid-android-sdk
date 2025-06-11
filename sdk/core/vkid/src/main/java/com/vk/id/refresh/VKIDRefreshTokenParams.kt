@file:OptIn(InternalVKIDApi::class)

package com.vk.id.refresh

import com.vk.id.common.InternalVKIDApi

/**
 * Create [VKIDRefreshTokenParams].
 *
 * @param initializer params' initialization.
 *
 * @since 2.0.0-alpha
 */
public inline fun VKIDRefreshTokenParams(
    initializer: VKIDRefreshTokenParams.Builder.() -> Unit,
): VKIDRefreshTokenParams {
    return VKIDRefreshTokenParams.Builder().apply(initializer).build()
}

/**
 * Encapsulated parameters for logout.
 *
 * @since 2.0.0-alpha
 */
public class VKIDRefreshTokenParams private constructor(
    internal val state: String?,
    internal val refreshAccessToken: Boolean,
) {
    /**
     * A builder for [VKIDRefreshTokenParams].
     *
     * @since 2.0.0-alpha
     */
    public class Builder {

        /**
         * An optional state to be passed to token refreshing.
         *
         * @since 2.0.0-alpha
         */
        public var state: String? = null

        /**
         * Whether to update access token on refresh
         */
        @InternalVKIDApi
        public var refreshAccessToken: Boolean = true

        /**
         * Constructs [VKIDRefreshTokenParams] with provided values.
         *
         * @since 2.0.0-alpha
         */
        public fun build(): VKIDRefreshTokenParams = VKIDRefreshTokenParams(
            state = state,
            refreshAccessToken = refreshAccessToken,
        )
    }
}
