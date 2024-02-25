package com.vk.id.exchangetoken

import com.vk.id.AccessToken

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
}
