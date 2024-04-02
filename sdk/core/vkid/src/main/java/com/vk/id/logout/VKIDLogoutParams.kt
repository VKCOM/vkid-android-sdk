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
public class VKIDLogoutParams private constructor() {
    /**
     * A builder for [VKIDLogoutParams].
     */
    public class Builder {
        /**
         * Constructs [VKIDLogoutParams] with provided values.
         */
        public fun build(): VKIDLogoutParams = VKIDLogoutParams()
    }
}
