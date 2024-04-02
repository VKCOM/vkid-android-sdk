package com.vk.id.exchangetoken

import com.vk.id.AccessToken
import com.vk.id.auth.AuthCodeData

/**
 * Callback interface for handling result of exchanging v1 token to v2.
 */
public interface VKIDExchangeTokenToV2Callback {
    /**
     * Called upon successful token exchange.
     *
     * @param token New token
     */
    public fun onSuccess(token: AccessToken)

    /**
     * Called upon any failure during token exchanging.
     *
     * @param fail Information about a failure.
     */
    public fun onFail(fail: VKIDExchangeTokenFail)

    /**
     * Call upon successful first step of auth for token exchange - receiving auth code which can later be exchanged to access token.
     */
    public fun onAuthCode(data: AuthCodeData): Unit = Unit
}
