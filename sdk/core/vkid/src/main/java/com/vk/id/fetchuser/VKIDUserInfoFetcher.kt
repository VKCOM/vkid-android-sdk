package com.vk.id.fetchuser

import com.vk.id.AccessToken
import com.vk.id.VKIDUser
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.toExpireTime
import com.vk.id.internal.state.StateGenerator
import com.vk.id.storage.TokenStorage

internal class VKIDUserInfoFetcher(
    private val api: VKIDApiService,
    private val stateGenerator: StateGenerator,
    private val serviceCredentials: ServiceCredentials,
    private val tokenStorage: TokenStorage,
) {
    fun fetch(
        payload: VKIDTokenPayload,
        onSuccess: (AccessToken) -> Unit,
        onFailedApiCall: (Throwable) -> Unit,
        onFailedOAuthState: () -> Unit,
    ) {
        val clientId = serviceCredentials.clientID
        val userInfoState = stateGenerator.regenerateState()
        val userInfoResult = api.getUserInfo(
            idToken = payload.idToken,
            clientId = serviceCredentials.clientID,
            deviceId = clientId,
            state = userInfoState
        ).execute()
        userInfoResult.onFailure(onFailedApiCall)
        userInfoResult.onSuccess {
            if (it.state != userInfoState) {
                onFailedOAuthState()
                return
            }
            val accessToken = AccessToken(
                token = payload.accessToken,
                userID = payload.userId,
                expireTime = payload.expiresIn.toExpireTime,
                userData = VKIDUser(
                    firstName = it.firstName,
                    lastName = it.lastName,
                    photo200 = it.avatar,
                    phone = it.phone,
                    email = it.email,
                )
            )
            tokenStorage.accessToken = accessToken
            tokenStorage.refreshToken = payload.refreshToken
            onSuccess(accessToken)
        }
    }
}
