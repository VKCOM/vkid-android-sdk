package com.vk.id

import com.vk.id.fetchuser.VKIDUserInfoFetcher
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.toExpireTime
import com.vk.id.storage.TokenStorage

internal class TokensHandler(
    private val userInfoFetcher: VKIDUserInfoFetcher,
    private val tokenStorage: TokenStorage,
) {
    fun handle(
        payload: VKIDTokenPayload,
        onSuccess: (AccessToken) -> Unit,
        onFailedApiCall: (Throwable) -> Unit,
        onFailedOAuthState: () -> Unit,
    ) {
        userInfoFetcher.fetch(
            idToken = payload.idToken,
            onSuccess = {
                val accessToken = AccessToken(
                    token = payload.accessToken,
                    userID = payload.userId,
                    expireTime = payload.expiresIn.toExpireTime,
                    userData = it
                )
                tokenStorage.accessToken = accessToken
                tokenStorage.refreshToken = payload.refreshToken
                onSuccess(accessToken)
            },
            onFailedApiCall = onFailedApiCall,
            onFailedOAuthState = onFailedOAuthState
        )
    }
}
