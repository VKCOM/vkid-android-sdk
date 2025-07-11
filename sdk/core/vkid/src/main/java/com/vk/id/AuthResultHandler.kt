@file:OptIn(InternalVKIDApi::class)

package com.vk.id

import com.vk.id.auth.AuthCodeData
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.logger.internalVKIDCreateLoggerForClass
import com.vk.id.logout.VKIDLoggerOut
import com.vk.id.logout.VKIDLogoutCallback
import com.vk.id.logout.VKIDLogoutFail
import com.vk.id.storage.InternalVKIDTokenStorage
import kotlinx.coroutines.withContext

@Suppress("LongParameterList")
internal class AuthResultHandler(
    private val dispatchers: VKIDCoroutinesDispatchers,
    private val callbacksHolder: AuthCallbacksHolder,
    private val deviceIdProvider: InternalVKIDDeviceIdProvider,
    private val prefsStore: InternalVKIDPrefsStore,
    private val serviceCredentials: ServiceCredentials,
    private val api: VKIDApiService,
    private val tokensHandler: TokensHandler,
    private val loggerOut: VKIDLoggerOut,
    private val tokenStorage: InternalVKIDTokenStorage,
) {

    private val logger = internalVKIDCreateLoggerForClass()

    internal suspend fun handle(
        authResult: AuthResult,
        onFail: () -> Unit
    ) {
        if (callbacksHolder.isEmpty()) {
            return
        }
        if (authResult !is AuthResult.Success) {
            emitAuthFail(authResult.toVKIDAuthFail(), onFail)
            prefsStore.clear()
            return
        }

        if (authResult.oauth != null) {
            handleOauth(authResult, onFail)
        } else {
            emitAuthFail(VKIDAuthFail.FailedOAuth("OAuth provider response does not have necessary OAuth data."), onFail)
        }
    }

    private suspend fun handleOauth(oauth: AuthResult.Success, onFail: () -> Unit) {
        val (realState, codeVerifier) = withContext(dispatchers.io) {
            deviceIdProvider.setDeviceId(oauth.deviceId)
            (prefsStore.state to prefsStore.codeVerifier).also { prefsStore.clear() }
        }

        if (realState != oauth.oauth?.state) {
            logger.error(
                "Invalid oauth state, want $realState but received ${oauth.oauth?.state}",
                null
            )
            emitAuthFail(VKIDAuthFail.FailedOAuthState("Invalid state"), onFail)
            return
        }

        callbacksHolder.getAll().forEach {
            val data = AuthCodeData(
                code = oauth.oauth.code,
                deviceId = oauth.deviceId
            )
            it.onAuthCode(data, isCompletion = codeVerifier.isBlank())
        }
        if (codeVerifier.isBlank()) {
            callbacksHolder.clear()
            return
        }

        // execute token request
        val callResult = withContext(dispatchers.io) {
            api.getToken(
                code = oauth.oauth.code,
                codeVerifier = codeVerifier,
                clientId = serviceCredentials.clientID,
                deviceId = oauth.deviceId,
                redirectUri = serviceCredentials.redirectUri,
                state = realState,
            ).execute()
        }
        callResult.onFailure {
            emitAuthFail(VKIDAuthFail.FailedApiCall("Failed code to token exchange api call: ${it.message}", it), onFail)
        }
        val accessToken = withContext(dispatchers.io) { tokenStorage.accessToken }
        callResult.onSuccess { payload ->
            tokensHandler.handle(
                payload = payload,
                onSuccess = {
                    if (accessToken != null) {
                        loggerOut.logout(
                            callback = object : VKIDLogoutCallback {
                                override fun onSuccess() = emitAuthSuccess(it)
                                override fun onFail(fail: VKIDLogoutFail) = emitAuthSuccess(it)
                            },
                            clearTokenStorage = false,
                            accessToken = accessToken.token,
                        )
                    } else {
                        emitAuthSuccess(it)
                    }
                },
                onFailedApiCall = {
                    emitAuthFail(VKIDAuthFail.FailedApiCall("Failed to fetch user data", it), onFail)
                },
            )
        }
    }

    private fun AuthResult.toVKIDAuthFail() = when (this) {
        is AuthResult.Canceled -> VKIDAuthFail.Canceled(message)
        is AuthResult.NoBrowserAvailable -> VKIDAuthFail.NoBrowserAvailable(message, error)
        is AuthResult.AuthActiviyResultFailed -> VKIDAuthFail.FailedRedirectActivity(message, error)
        is AuthResult.Success -> error("AuthResult is Success and cannot be converted to fail!")
    }

    private fun emitAuthSuccess(token: AccessToken) {
        callbacksHolder.getAll().forEach {
            it.onAuth(token)
        }
        callbacksHolder.clear()
    }

    private fun emitAuthFail(fail: VKIDAuthFail, onFail: () -> Unit) {
        callbacksHolder.getAll().forEach {
            it.onFail(fail)
        }
        onFail()
        callbacksHolder.clear()
    }
}
