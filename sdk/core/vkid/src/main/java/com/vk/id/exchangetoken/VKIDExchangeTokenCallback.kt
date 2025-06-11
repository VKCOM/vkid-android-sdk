package com.vk.id.exchangetoken

import com.vk.id.AccessToken
import com.vk.id.auth.AuthCodeData

/**
 * Callback interface for handling result of exchanging v1 token to v2.
 *
 * @since 2.0.0-alpha
 */
public interface VKIDExchangeTokenCallback {
    /**
     * Called upon successful token exchange.
     *
     * @param accessToken access token and other useful data.
     *
     * @since 2.0.0-alpha
     */
    public fun onAuth(accessToken: AccessToken)

    /**
     * Called upon any failure during token exchanging.
     *
     * @param fail Information about a failure.
     *
     * @since 2.0.0-alpha
     */
    public fun onFail(fail: VKIDExchangeTokenFail)

    /**
     * Call upon successful first step of auth for token exchange - receiving auth code which can later be exchanged to access token.
     *
     * @param data auth code that can be exchanged for access token
     * @param isCompletion true if [onAuth] won't be called.
     * This will happen if you passed auth parameters and implement their validation yourself.
     * In that case we can't exchange auth code for access token and you should do this yourself.
     *
     * @since 2.0.0-alpha
     */
    public fun onAuthCode(data: AuthCodeData, isCompletion: Boolean): Unit = Unit
}
