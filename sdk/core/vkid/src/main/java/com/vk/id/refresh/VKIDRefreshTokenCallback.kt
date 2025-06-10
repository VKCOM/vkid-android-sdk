package com.vk.id.refresh

import com.vk.id.AccessToken

/**
 * Callback interface for handling token refreshing result.
 *
 * @since 2.0.0-alpha
 */
public interface VKIDRefreshTokenCallback {
    /**
     * Called upon successful token refreshing.
     *
     * @param token New token
     *
     * @since 2.0.0-alpha
     */
    public fun onSuccess(token: AccessToken)

    /**
     * Called upon any failure during token refreshing.
     *
     * @param fail Information about a failure.
     *
     * @since 2.0.0-alpha
     */
    public fun onFail(fail: VKIDRefreshTokenFail)
}
