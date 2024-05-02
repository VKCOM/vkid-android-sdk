package com.vk.id.exchangetoken

import com.vk.id.AccessToken
import com.vk.id.auth.AuthCodeData

/**
 * Callback interface for handling result of exchanging v1 token to v2.
 */
public interface VKIDExchangeTokenCallback {
    /**
     * Called upon successful token exchange.
     *
     * @param accessToken access token and other useful data.
     */
    public fun onAuth(accessToken: AccessToken)

    /**
     * Called upon any failure during token exchanging.
     *
     * @param fail Information about a failure.
     */
    public fun onFail(fail: VKIDExchangeTokenFail)

    /**
     * Call upon successful first step of auth for token exchange - receiving auth code which can later be exchanged to access token.
     *
     * @param data auth code that can be exchanged for access token
     * @param isCompletion true if [onAuth] won't be called.
     * This will happen if you passed auth parameters and implement their validation yourself.
     * In that case we can't exchange auth code for access token and you should do this yourself.
     */
    public fun onAuthCode(data: AuthCodeData, isCompletion: Boolean): Unit = Unit
}
