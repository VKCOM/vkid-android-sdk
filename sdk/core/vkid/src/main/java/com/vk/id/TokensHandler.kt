package com.vk.id

import com.vk.id.fetchuser.VKIDUserInfoFetcher
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.toExpireTime
import com.vk.id.storage.TokenStorage

internal class TokensHandler(
    private val userInfoFetcher: VKIDUserInfoFetcher,
    private val tokenStorage: TokenStorage,
) {
    suspend fun handle(
        payload: VKIDTokenPayload,
        onSuccess: (AccessToken) -> Unit,
        onFailedApiCall: (Throwable) -> Unit,
        onFailedOAuthState: () -> Unit,
    ) {
        userInfoFetcher.fetch(
            accessToken = payload.accessToken,
            onSuccess = {
                val accessToken = AccessToken(
                    token = payload.accessToken,
                    idToken = payload.idToken,
                    userID = payload.userId,
                    expireTime = payload.expiresIn.toExpireTime,
                    userData = it
                )
                tokenStorage.accessToken = accessToken
                tokenStorage.idToken = payload.idToken
                tokenStorage.refreshToken = payload.refreshToken
                onSuccess(accessToken)
            },
            onFailedApiCall = onFailedApiCall,
            onFailedOAuthState = onFailedOAuthState
        )
    }
}
