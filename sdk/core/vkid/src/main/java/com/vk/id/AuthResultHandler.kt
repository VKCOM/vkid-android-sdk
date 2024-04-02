package com.vk.id

import com.vk.id.auth.AuthCodeData
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.log.createLoggerForClass
import com.vk.id.internal.store.PrefsStore
import com.vk.id.internal.util.currentTime
import kotlinx.coroutines.withContext

@Suppress("LongParameterList")
internal class AuthResultHandler(
    private val dispatchers: CoroutinesDispatchers,
    private val callbacksHolder: AuthCallbacksHolder,
    private val deviceIdProvider: DeviceIdProvider,
    private val prefsStore: PrefsStore,
    private val serviceCredentials: ServiceCredentials,
    private val api: VKIDApiService,
    private val tokensHandler: TokensHandler
) {

    private val logger = createLoggerForClass()

    internal suspend fun handle(
        authResult: AuthResult
    ) {
        if (authResult !is AuthResult.Success) {
            emitAuthFail(authResult.toVKIDAuthFail())
            return
        }
        // We do not stop auth here in hope that it still be success,
        // but if not there will be error response from backend
        if (authResult.expireTime < currentTime()) {
            logger.error("OAuth code is old, there is a big chance auth will fail", null)
        }

        if (authResult.oauth != null) {
            handleOauth(authResult)
        } else {
            emitAuthFail(VKIDAuthFail.FailedOAuth("OAuth provider response does not have necessary OAuth data."))
        }
    }

    private suspend fun handleOauth(oauth: AuthResult.Success) {
        deviceIdProvider.setDeviceId(oauth.deviceId)
        val (realState, codeVerifier) = withContext(dispatchers.io) {
            (prefsStore.state to prefsStore.codeVerifier).also { prefsStore.clear() }
        }

        if (realState != oauth.oauth?.state) {
            logger.error(
                "Invalid oauth state, want $realState but received ${oauth.oauth?.state}",
                null
            )
            emitAuthFail(VKIDAuthFail.FailedOAuthState("Invalid state"))
            return
        }

        callbacksHolder.getAll().forEach { it.onAuthCode(AuthCodeData(oauth.oauth.code)) }
        if (codeVerifier.isBlank()) {
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
            emitAuthFail(
                VKIDAuthFail.FailedApiCall(
                    "Failed code to token exchange api call: ${it.message}",
                    it
                )
            )
        }
        callResult.onSuccess { payload ->
            tokensHandler.handle(
                payload = payload,
                onSuccess = ::emitAuthSuccess,
                onFailedApiCall = {
                    emitAuthFail(VKIDAuthFail.FailedApiCall("Failed to fetch user data", it))
                },
            )
        }
    }

    private fun AuthResult.toVKIDAuthFail() = when (this) {
        is AuthResult.Canceled -> VKIDAuthFail.Canceled(message)
        is AuthResult.NoBrowserAvailable -> VKIDAuthFail.NoBrowserAvailable(
            message,
            error
        )

        is AuthResult.AuthActiviyResultFailed -> VKIDAuthFail.FailedRedirectActivity(
            message,
            error
        )

        is AuthResult.Success -> error("AuthResult is Success and cannot be converted to fail!")
    }

    private fun emitAuthSuccess(token: AccessToken) {
        callbacksHolder.getAll().forEach {
            it.onSuccess(token)
        }
        callbacksHolder.clear()
    }

    private fun emitAuthFail(fail: VKIDAuthFail) {
        callbacksHolder.getAll().forEach {
            it.onFail(fail)
        }
        callbacksHolder.clear()
    }
}
