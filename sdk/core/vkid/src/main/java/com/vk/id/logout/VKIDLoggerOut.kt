@file:OptIn(InternalVKIDApi::class)

package com.vk.id.logout

import com.vk.id.AccessToken
import com.vk.id.VKIDInvalidTokenException
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refresh.VKIDRefreshTokenParams
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.storage.TokenStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class VKIDLoggerOut(
    private val api: VKIDApiService,
    private val tokenStorage: TokenStorage,
    private val deviceIdProvider: DeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val dispatchers: CoroutinesDispatchers,
    private val tokenRefresher: VKIDTokenRefresher,
) {
    suspend fun logout(
        callback: VKIDLogoutCallback,
        @Suppress("UnusedParameter")
        params: VKIDLogoutParams = VKIDLogoutParams {},
    ) {
        val (token, deviceId) = withContext(dispatchers.io) {
            tokenStorage.accessToken?.token to deviceIdProvider.getDeviceId()
        }
        token ?: run {
            withContext(dispatchers.io) { tokenStorage.clear() }
            callback.onFail(VKIDLogoutFail.NotAuthenticated("Not authorized, can't logout"))
            return
        }
        val clientId = serviceCredentials.clientID
        withContext(dispatchers.io) {
            api.logout(
                accessToken = token,
                deviceId = deviceId,
                clientId = clientId,
            ).execute()
        }.onFailure {
            handleLogoutFailure(
                callback = callback,
                params = params,
                deviceId = deviceId,
                clientId = clientId,
                throwable = it,
            )
        }.onSuccess {
            withContext(dispatchers.io) { tokenStorage.clear() }
            callback.onSuccess()
        }
    }

    private suspend fun handleLogoutFailure(
        callback: VKIDLogoutCallback,
        params: VKIDLogoutParams,
        deviceId: String,
        clientId: String,
        throwable: Throwable
    ) {
        if (throwable is VKIDInvalidTokenException) {
            val coroutinesContext = currentCoroutineContext()
            tokenRefresher.refresh(
                callback = object : VKIDRefreshTokenCallback {
                    override fun onSuccess(token: AccessToken) {
                        onTokenRefreshed(
                            callback = callback,
                            coroutinesContext = coroutinesContext,
                            token = token,
                            deviceId = deviceId,
                            clientId = clientId,
                        )
                    }

                    override fun onFail(fail: VKIDRefreshTokenFail) {
                        CoroutineScope(Job() + coroutinesContext).launch(dispatchers.io) { tokenStorage.clear() }
                        callback.onFail(VKIDLogoutFail.FailedApiCall("Failed to logout and refresh token", throwable))
                    }
                },
                params = VKIDRefreshTokenParams {
                    this.state = params.refreshTokenState
                }
            )
        } else {
            callback.onFail(VKIDLogoutFail.FailedApiCall("Failed to logout due to ${throwable.message}", throwable))
        }
    }

    private fun onTokenRefreshed(
        callback: VKIDLogoutCallback,
        coroutinesContext: CoroutineContext,
        token: AccessToken,
        deviceId: String,
        clientId: String,
    ) {
        CoroutineScope(Job() + coroutinesContext).launch {
            withContext(dispatchers.io) {
                api.logout(
                    accessToken = token.token,
                    deviceId = deviceId,
                    clientId = clientId,
                ).execute()
            }.onFailure {
                withContext(dispatchers.io) { tokenStorage.clear() }
                callback.onFail(VKIDLogoutFail.FailedApiCall("Failed to logout due to ${it.message}", it))
            }.onSuccess {
                withContext(dispatchers.io) { tokenStorage.clear() }
                callback.onSuccess()
            }
        }
    }
}
