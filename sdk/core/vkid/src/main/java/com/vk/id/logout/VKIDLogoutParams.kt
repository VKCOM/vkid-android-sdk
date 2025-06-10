package com.vk.id.logout

/**
 * Create [VKIDLogoutParams].
 *
 * @param initializer params' initialization.
 *
 * @since 2.0.0-alpha
 */
public inline fun VKIDLogoutParams(
    initializer: VKIDLogoutParams.Builder.() -> Unit,
): VKIDLogoutParams {
    return VKIDLogoutParams.Builder().apply(initializer).build()
}

/**
 * Encapsulated parameters for logout.
 *
 * @since 2.0.0-alpha
 */
public class VKIDLogoutParams private constructor() {
    /**
     * A builder for [VKIDLogoutParams].
     *
     * @since 2.0.0-alpha
     */
    public class Builder {

        /**
         * Constructs [VKIDLogoutParams] with provided values.
         *
         * @since 2.0.0-alpha
         */
        public fun build(): VKIDLogoutParams = VKIDLogoutParams()
    }
}
