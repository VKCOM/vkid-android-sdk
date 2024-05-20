package com.vk.id.refresh

import com.vk.id.AccessToken

/**
 * Callback interface for handling token refreshing result.
 */
public interface VKIDRefreshTokenCallback {
    /**
     * Called upon successful token refreshing.
     *
     * @param token New token
     */
    public fun onSuccess(token: AccessToken)

    /**
     * Called upon any failure during token refreshing.
     *
     * @param fail Information about a failure.
     */
    public fun onFail(fail: VKIDRefreshTokenFail)
}
