@file:OptIn(InternalVKIDApi::class)

package com.vk.id.refresh

import com.vk.id.TokensHandler
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import com.vk.id.storage.TokenStorage
import kotlinx.coroutines.withContext

@Suppress("LongParameterList")
internal class VKIDTokenRefresher(
    private val api: VKIDApiService,
    private val tokenStorage: TokenStorage,
    private val deviceIdProvider: DeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val stateGenerator: StateGenerator,
    private val tokensHandler: TokensHandler,
    private val dispatchers: CoroutinesDispatchers,
) {
    suspend fun refresh(
        callback: VKIDRefreshTokenCallback,
        params: VKIDRefreshTokenParams = VKIDRefreshTokenParams {},
    ) {
        val deviceId = deviceIdProvider.getDeviceId()
        val clientId = serviceCredentials.clientID
        val refreshTokenState = params.state ?: stateGenerator.regenerateState()
        val refreshToken = tokenStorage.refreshToken ?: return emitUnauthorizedFail(callback)

        val result = withContext(dispatchers.io) {
            api.refreshToken(
                refreshToken = refreshToken,
                deviceId = deviceId,
                clientId = clientId,
                state = refreshTokenState,
            ).execute()
        }
        result.onSuccess { payload ->
            if (payload.state != refreshTokenState) {
                callback.onFail(VKIDRefreshTokenFail.FailedOAuthState("Wrong state for getting user info"))
            }
            tokensHandler.handle(
                payload = payload,
                onSuccess = callback::onSuccess,
                onFailedApiCall = {
                    callback.onFail(
                        VKIDRefreshTokenFail.FailedApiCall("Failed to fetch user data due to ${it.message}", it)
                    )
                },
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
    ) = callback.onFail(VKIDRefreshTokenFail.NotAuthenticated("You must login before refreshing token"))
}
