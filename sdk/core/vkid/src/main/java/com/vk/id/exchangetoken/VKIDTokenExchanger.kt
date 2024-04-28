@file:OptIn(InternalVKIDApi::class)

package com.vk.id.exchangetoken

import com.vk.id.TokensHandler
import com.vk.id.auth.AuthCodeData
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import com.vk.id.internal.store.InternalVKIDPrefsStore
import kotlinx.coroutines.withContext
import java.security.SecureRandom

@Suppress("LongParameterList")
internal class VKIDTokenExchanger(
    private val api: VKIDApiService,
    private val deviceIdProvider: InternalVKIDDeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val stateGenerator: StateGenerator,
    private val tokensHandler: TokensHandler,
    private val dispatchers: VKIDCoroutinesDispatchers,
    private val prefsStore: InternalVKIDPrefsStore,
    private val pkceGenerator: PkceGeneratorSHA256,
) {
    suspend fun exchange(
        v1Token: String,
        params: VKIDExchangeTokenParams,
        callback: VKIDExchangeTokenCallback,
    ) {
        val deviceId = withContext(dispatchers.io) { deviceIdProvider.getDeviceId() }
        val clientId = serviceCredentials.clientID
        val state = params.state ?: stateGenerator.regenerateState()
        val (codeVerifier, codeChallenge) = if (params.codeChallenge == null) {
            val codeVerifier = pkceGenerator.generateRandomCodeVerifier(SecureRandom())
            (codeVerifier to pkceGenerator.deriveCodeVerifierChallenge(codeVerifier))
        } else {
            null to params.codeChallenge
        }
        val result = withContext(dispatchers.io) {
            api.exchangeToken(
                v1Token = v1Token,
                clientId = clientId,
                deviceId = deviceId,
                state = state,
                codeChallenge = codeChallenge,
            ).execute()
        }
        result.onFailure {
            prefsStore.clear()
            callback.onFail(VKIDExchangeTokenFail.FailedApiCall("Failed to exchange token due to: ${it.message}", it))
        }
        result.onSuccess { payload ->
            prefsStore.clear()
            if (payload.state != state) {
                callback.onFail(VKIDExchangeTokenFail.FailedOAuthState("Invalid state during code receiving"))
                return
            }
            withContext(dispatchers.io) { deviceIdProvider.setDeviceId(payload.deviceId) }
            callback.onAuthCode(AuthCodeData(payload.code), codeVerifier == null)
            if (codeVerifier == null) return
            handleCode(
                code = payload.code,
                codeVerifier = codeVerifier,
                params = params,
                callback = callback,
            )
        }
    }

    private suspend fun handleCode(
        code: String,
        codeVerifier: String,
        params: VKIDExchangeTokenParams,
        callback: VKIDExchangeTokenCallback,
    ) {
        val state = params.codeExchangeState ?: withContext(dispatchers.io) { stateGenerator.regenerateState() }
        val callResult = withContext(dispatchers.io) {
            api.getToken(
                code = code,
                codeVerifier = codeVerifier,
                clientId = serviceCredentials.clientID,
                deviceId = deviceIdProvider.getDeviceId(),
                redirectUri = serviceCredentials.redirectUri,
                state = state,
            ).execute()
        }
        callResult.onFailure {
            prefsStore.clear()
            callback.onFail(VKIDExchangeTokenFail.FailedApiCall("Failed to exchange code due to: ${it.message}", it))
        }
        callResult.onSuccess { payload ->
            prefsStore.clear()
            if (payload.state != state) {
                callback.onFail(VKIDExchangeTokenFail.FailedOAuthState("Invalid state during code exchange"))
                return
            }
            tokensHandler.handle(
                payload = payload,
                onSuccess = callback::onSuccess,
                onFailedApiCall = {
                    callback.onFail(VKIDExchangeTokenFail.FailedApiCall("Failed to fetch user data", it))
                },
            )
        }
    }
}
