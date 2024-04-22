package com.vk.id.logout

/**
 * Create [VKIDLogoutParams].
 *
 * @param initializer params' initialization.
 */
public inline fun VKIDLogoutParams(
    initializer: VKIDLogoutParams.Builder.() -> Unit,
): VKIDLogoutParams {
    return VKIDLogoutParams.Builder().apply(initializer).build()
}

/**
 * Encapsulated parameters for logout.
 */
public class VKIDLogoutParams private constructor(
    internal val refreshTokenState: String? = null,
) {
    /**
     * A builder for [VKIDLogoutParams].
     */
    public class Builder {

        /**
         * An optional state to be passed to token refreshing.
         * Token refreshing might happen under the hood while logging out.
         * If you want to pass an external state to this process you may do so in this property.
         */
        public var refreshTokenState: String? = null

        /**
         * Constructs [VKIDLogoutParams] with provided values.
         */
        public fun build(): VKIDLogoutParams = VKIDLogoutParams(
            refreshTokenState = refreshTokenState,
        )
    }
}
