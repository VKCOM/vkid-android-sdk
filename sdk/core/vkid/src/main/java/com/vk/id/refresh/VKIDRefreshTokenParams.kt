@file:OptIn(InternalVKIDApi::class)

package com.vk.id.refresh

import com.vk.id.common.InternalVKIDApi

/**
 * Create [VKIDRefreshTokenParams].
 *
 * @param initializer params' initialization.
 */
public inline fun VKIDRefreshTokenParams(
    initializer: VKIDRefreshTokenParams.Builder.() -> Unit,
): VKIDRefreshTokenParams {
    return VKIDRefreshTokenParams.Builder().apply(initializer).build()
}

/**
 * Encapsulated parameters for logout.
 */
public class VKIDRefreshTokenParams private constructor(
    internal val state: String?,
    internal val refreshAccessToken: Boolean,
) {
    /**
     * A builder for [VKIDRefreshTokenParams].
     */
    public class Builder {

        /**
         * An optional state to be passed to token refreshing.
         */
        public var state: String? = null

        /**
         * Whether to update access token on refresh
         */
        @InternalVKIDApi
        public var refreshAccessToken: Boolean = true

        /**
         * Constructs [VKIDRefreshTokenParams] with provided values.
         */
        public fun build(): VKIDRefreshTokenParams = VKIDRefreshTokenParams(
            state = state,
            refreshAccessToken = refreshAccessToken,
        )
    }
}
