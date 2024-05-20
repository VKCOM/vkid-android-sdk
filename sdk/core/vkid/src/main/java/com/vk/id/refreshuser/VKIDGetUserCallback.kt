package com.vk.id.refreshuser

import com.vk.id.VKIDUser

/**
 * Callback interface for refreshing user data.
 */
public interface VKIDGetUserCallback {
    /**
     * Called upon successful user refreshing.
     *
     * @param user Up-to-date user data. Might not contain all fields if the user didn't accept necessary scopes.
     */
    public fun onSuccess(user: VKIDUser)

    /**
     * Called upon any failure during user refreshing.
     *
     * @param fail Information about a failure.
     */
    public fun onFail(fail: VKIDGetUserFail)
}
