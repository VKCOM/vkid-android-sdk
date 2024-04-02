package com.vk.id.auth

import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail

/**
 * Callback interface for handling the authentication result.
 */
public interface VKIDAuthCallback {

    /**
     * Called upon successful auth.
     * Won't be called if you passed [VKIDAuthParams.codeChallenge] because in that case we can't validate it.
     * You will receive auth code in [onAuthCode] and can exchange it to the access token yourself.
     */
    public fun onSuccess(accessToken: AccessToken)

    /**
     * Call upon successful first step of auth - receiving auth code which can later be exchanged to access token.
     */
    public fun onAuthCode(data: AuthCodeData): Unit = Unit

    /**
     * Called upon any failure during auth.
     */
    public fun onFail(fail: VKIDAuthFail)
}
