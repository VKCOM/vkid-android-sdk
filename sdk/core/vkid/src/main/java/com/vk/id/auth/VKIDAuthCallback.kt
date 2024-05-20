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
     *
     * @param accessToken access token and other useful data.
     */
    public fun onAuth(accessToken: AccessToken)

    /**
     * Called upon successful first step of auth - receiving auth code which can later be exchanged to access token.
     *
     * @param data auth code that can be exchanged for access token
     * @param isCompletion true if [onAuth] won't be called.
     * This will happen if you passed auth parameters and implement their validation yourself.
     * In that case we can't exchange auth code for access token and you should do this yourself.
     */
    public fun onAuthCode(data: AuthCodeData, isCompletion: Boolean): Unit = Unit

    /**
     * Called upon any failure during auth.
     *
     * @param fail Information about a failure.
     */
    public fun onFail(fail: VKIDAuthFail)
}
