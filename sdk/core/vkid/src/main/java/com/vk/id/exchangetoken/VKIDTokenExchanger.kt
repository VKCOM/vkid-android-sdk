package com.vk.id.exchangetoken

import android.content.Context
import com.vk.id.TokensHandler
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import kotlinx.coroutines.withContext

@Suppress("LongParameterList")
internal class VKIDTokenExchanger(
    private val context: Context,
    private val api: VKIDApiService,
    private val deviceIdProvider: DeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val stateGenerator: StateGenerator,
    private val tokensHandler: TokensHandler,
    private val dispatchers: CoroutinesDispatchers,
) {
    suspend fun exchange(v1Token: String, callback: VKIDExchangeTokenToV2Callback) {
        val deviceId = deviceIdProvider.getDeviceId(context)
        val clientId = serviceCredentials.clientID
        val state = stateGenerator.regenerateState()
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
            tokensHandler.handle(
                payload = payload,
                onSuccess = callback::onSuccess,
                onFailedApiCall = {
                    callback.onFail(
                        VKIDExchangeTokenFail.FailedApiCall("Failed to fetch user data due to ${it.message}", it)
                    )
                },
                onFailedOAuthState = {
                    callback.onFail(VKIDExchangeTokenFail.FailedOAuthState("Wrong state for getting user info"))
                }
            )
        }
    }
}
