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
        state: String?,
        onSuccess: (AccessToken) -> Unit,
        onFailedApiCall: (Throwable) -> Unit,
    ) {
        userInfoFetcher.fetch(
            accessToken = payload.accessToken,
            state = state,
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
        )
    }
}
