package com.vk.id.logout

/**
 * Callback interface for logging out.
 *
 * @since 2.0.0-alpha
 */
public interface VKIDLogoutCallback {
    /**
     * Called upon successful logout.
     *
     * @since 2.0.0-alpha
     */
    public fun onSuccess()

    /**
     * Called upon any failure during logout.
     *
     * @param fail Information about a failure.
     *
     * @since 2.0.0-alpha
     */
    public fun onFail(fail: VKIDLogoutFail)
}
