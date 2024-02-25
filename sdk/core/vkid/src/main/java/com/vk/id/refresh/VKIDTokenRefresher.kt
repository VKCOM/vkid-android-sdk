package com.vk.id.refresh

import android.content.Context
import com.vk.id.fetchuser.VKIDUserInfoFetcher
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.state.StateGenerator
import com.vk.id.storage.TokenStorage

@Suppress("LongParameterList")
internal class VKIDTokenRefresher(
    private val context: Context,
    private val api: VKIDApiService,
    private val tokenStorage: TokenStorage,
    private val deviceIdProvider: DeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val stateGenerator: StateGenerator,
    private val userInfoFetcher: VKIDUserInfoFetcher,
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
            userInfoFetcher.fetch(
                payload = payload,
                onSuccess = callback::onSuccess,
                onFailedApiCall = {
                    callback.onFail(
                        VKIDRefreshTokenFail.FailedApiCall("Failed to fetch user data due to ${it.message}", it)
                    )
                },
                onFailedOAuthState = {
                    callback.onFail(VKIDRefreshTokenFail.FailedOAuthState("Wrong state for getting user info"))
                }
            )
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
