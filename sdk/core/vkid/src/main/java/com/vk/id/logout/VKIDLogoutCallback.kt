package com.vk.id.logout

/**
 * Callback interface for logging out.
 */
public interface VKIDLogoutCallback {
    /**
     * Called upon successful logout.
     */
    public fun onSuccess()

    /**
     * Called upon any failure during logout.
     *
     * @param fail Information about a failure.
     */
    public fun onFail(fail: VKIDLogoutFail)
}
