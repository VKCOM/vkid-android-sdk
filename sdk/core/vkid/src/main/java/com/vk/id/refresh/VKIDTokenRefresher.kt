@file:OptIn(InternalVKIDApi::class)

package com.vk.id.refresh

import com.vk.id.TokensHandler
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.storage.InternalVKIDTokenStorage
import kotlinx.coroutines.withContext

@Suppress("LongParameterList")
internal class VKIDTokenRefresher(
    private val api: VKIDApiService,
    private val tokenStorage: InternalVKIDTokenStorage,
    private val deviceIdProvider: InternalVKIDDeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val stateGenerator: StateGenerator,
    private val tokensHandler: TokensHandler,
    private val dispatchers: VKIDCoroutinesDispatchers,
    private val prefsStore: InternalVKIDPrefsStore,
) {
    suspend fun refresh(
        callback: VKIDRefreshTokenCallback,
        params: VKIDRefreshTokenParams = VKIDRefreshTokenParams {},
    ) {
        val clientId = serviceCredentials.clientID
        val refreshToken = withContext(dispatchers.io) {
            tokenStorage.refreshToken
        } ?: return emitUnauthorizedFail(callback)
        val (refreshTokenState, deviceId) = withContext(dispatchers.io) {
            (params.state ?: stateGenerator.regenerateState()) to deviceIdProvider.getDeviceId()
        }

        val result = withContext(dispatchers.io) {
            api.refreshToken(
                refreshToken = refreshToken.token,
                deviceId = deviceId,
                clientId = clientId,
                state = refreshTokenState,
            ).execute()
        }
        result.onSuccess { payload ->
            if (payload.state != refreshTokenState) {
                prefsStore.clear()
                callback.onFail(VKIDRefreshTokenFail.FailedOAuthState("Wrong state for getting user info"))
                return
            }
            tokensHandler.handle(
                payload = payload,
                onSuccess = {
                    prefsStore.clear()
                    callback.onSuccess(it)
                },
                onFailedApiCall = {
                    prefsStore.clear()
                    callback.onFail(
                        VKIDRefreshTokenFail.FailedApiCall("Failed to fetch user data due to: ${it.message}", it)
                    )
                },
                refreshAccessToken = params.refreshAccessToken,
            )
        }
        result.onFailure {
            prefsStore.clear()
            callback.onFail(
                VKIDRefreshTokenFail.FailedApiCall("Failed code to refresh token due to: ${it.message}", it)
            )
        }
    }

    private fun emitUnauthorizedFail(
        callback: VKIDRefreshTokenCallback
    ) = callback.onFail(VKIDRefreshTokenFail.NotAuthenticated("You must login before refreshing token"))
}
