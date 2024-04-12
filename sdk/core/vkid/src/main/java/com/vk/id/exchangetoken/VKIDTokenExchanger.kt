@file:OptIn(InternalVKIDApi::class)

package com.vk.id.exchangetoken

import com.vk.id.TokensHandler
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import kotlinx.coroutines.withContext

@Suppress("LongParameterList")
internal class VKIDTokenExchanger(
    private val api: VKIDApiService,
    private val deviceIdProvider: DeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val stateGenerator: StateGenerator,
    private val tokensHandler: TokensHandler,
    private val dispatchers: CoroutinesDispatchers,
) {
    suspend fun exchange(
        v1Token: String,
        params: VKIDExchangeTokenParams,
        callback: VKIDExchangeTokenCallback,
    ) {
        val deviceId = deviceIdProvider.getDeviceId()
        val clientId = serviceCredentials.clientID
        val state = params.state ?: stateGenerator.regenerateState()
        val result = withContext(dispatchers.io) {
            api.exchangeToken(
                v1Token = v1Token,
                clientId = clientId,
                deviceId = deviceId,
                state = state
            ).execute()
        }
        result.onFailure {
            callback.onFail(
                VKIDExchangeTokenFail.FailedApiCall("Failed code to refresh token due to: ${it.message}", it)
            )
        }
        result.onSuccess { payload ->
            if (payload.state != state) {
                callback.onFail(VKIDExchangeTokenFail.FailedOAuthState("Invalid state"))
                return
            }
            tokensHandler.handle(
                payload = payload,
                onSuccess = callback::onSuccess,
                onFailedApiCall = {
                    callback.onFail(
                        VKIDExchangeTokenFail.FailedApiCall("Failed to fetch user data due to ${it.message}", it)
                    )
                },
            )
        }
    }
}
