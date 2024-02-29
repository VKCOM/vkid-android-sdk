package com.vk.id.refresh

import android.content.Context
import com.vk.id.AccessToken
import com.vk.id.VKIDUser
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.toExpireTime
import com.vk.id.internal.state.StateGenerator
import com.vk.id.storage.TokenStorage

internal class VKIDTokenRefresher(
    private val context: Context,
    private val api: VKIDApiService,
    private val tokenStorage: TokenStorage,
    private val deviceIdProvider: DeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val stateGenerator: StateGenerator,
) {
    fun refresh(callback: VKIDRefreshTokenCallback) {
        val deviceId = deviceIdProvider.getDeviceId(context)
        val clientId = serviceCredentials.clientID
        val refreshTokenState = stateGenerator.regenerateState()
        val result = api.refreshToken(
            refreshToken = tokenStorage.refreshToken ?: return emitUnauthorizedFail(callback),
            deviceId = deviceId,
            clientId = clientId,
            state = refreshTokenState,
        ).execute()
        result.onSuccess { payload ->
            val userInfoState = stateGenerator.regenerateState()
            val userInfoResult = api.getUserInfo(
                idToken = payload.idToken,
                clientId = serviceCredentials.clientID,
                deviceId = clientId,
                state = userInfoState
            ).execute()
            userInfoResult.onFailure {
                callback.onFail(
                    VKIDRefreshTokenFail.FailedApiCall("Failed to fetch user data due to ${it.message}", it)
                )
            }
            userInfoResult.onSuccess {
                if (it.state != userInfoState) {
                    callback.onFail(VKIDRefreshTokenFail.FailedOAuthState("Wrong state for getting user info"))
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
                    )
                )
                tokenStorage.accessToken = accessToken
                tokenStorage.refreshToken = payload.refreshToken
                callback.onSuccess(accessToken)
            }
        }
        result.onFailure {
            callback.onFail(
                VKIDRefreshTokenFail.FailedApiCall("Failed code to refresh token due to: ${it.message}", it)
            )
        }
    }

    private fun emitUnauthorizedFail(
        callback: VKIDRefreshTokenCallback
    ) = callback.onFail(VKIDRefreshTokenFail.Unauthorized("You must login before refreshing token"))
}
