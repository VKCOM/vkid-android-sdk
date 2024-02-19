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
        val result = api.refreshToken(
            refreshToken = tokenStorage.refreshToken ?: return emitUnauthorizedFail(callback),
            deviceId = deviceIdProvider.getDeviceId(context),
            clientId = serviceCredentials.clientID,
            state = stateGenerator.regenerateState()
        ).execute()
        result.onSuccess {
            // todo: Unify with AuthResultHandler in VKIDSDK-792
            val accessToken = AccessToken(
                token = it.accessToken,
                userID = it.userId,
                expireTime = it.expiresIn.toExpireTime,
                userData = VKIDUser(
                    firstName = "",
                    lastName = "",
                    photo200 = "",
                    phone = it.phone,
                    email = it.email
                )
            )
            tokenStorage.accessToken = accessToken
            tokenStorage.refreshToken = it.refreshToken
            callback.onSuccess(accessToken)
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
