@file:OptIn(InternalVKIDApi::class)

package com.vk.id

import com.vk.id.common.InternalVKIDApi
import com.vk.id.fetchuser.VKIDUserInfoFetcher
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.toExpireTime
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.storage.TokenStorage
import kotlinx.coroutines.withContext

internal class TokensHandler(
    private val userInfoFetcher: VKIDUserInfoFetcher,
    private val tokenStorage: TokenStorage,
    private val dispatchers: VKIDCoroutinesDispatchers,
) {
    suspend fun handle(
        payload: VKIDTokenPayload,
        onSuccess: suspend (AccessToken) -> Unit,
        onFailedApiCall: (Throwable) -> Unit,
        refreshAccessToken: Boolean = true,
    ) {
        userInfoFetcher.fetch(
            accessToken = payload.accessToken,
            onSuccess = {
                val accessToken = withContext(dispatchers.io) {
                    AccessToken(
                        token = payload.accessToken,
                        idToken = payload.idToken.takeIf { it.isNotBlank() } ?: tokenStorage.accessToken?.idToken,
                        userID = payload.userId,
                        expireTime = payload.expiresIn.toExpireTime,
                        userData = it
                    )
                }
                withContext(dispatchers.io) {
                    if (refreshAccessToken) tokenStorage.accessToken = accessToken
                    tokenStorage.refreshToken = payload.refreshToken
                }
                onSuccess(accessToken)
            },
            onFailedApiCall = onFailedApiCall,
        )
    }
}
