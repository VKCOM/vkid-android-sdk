package com.vk.id.refreshuser

import com.vk.id.VKIDUser

/**
 * Callback interface for refreshing user data.
 *
 * @since 2.0.0-alpha
 */
public interface VKIDGetUserCallback {
    /**
     * Called upon successful user refreshing.
     *
     * @param user Up-to-date user data. Might not contain all fields if the user didn't accept necessary scopes.
     *
     * @since 2.0.0-alpha
     */
    public fun onSuccess(user: VKIDUser)

    /**
     * Called upon any failure during user refreshing.
     *
     * @param fail Information about a failure.
     *
     * @since 2.0.0-alpha
     */
    public fun onFail(fail: VKIDGetUserFail)
}
