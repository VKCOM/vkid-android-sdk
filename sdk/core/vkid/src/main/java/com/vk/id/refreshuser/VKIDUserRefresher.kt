package com.vk.id.refreshuser

import com.vk.id.AccessToken
import com.vk.id.VKIDInvalidTokenException
import com.vk.id.VKIDUser
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.api.dto.VKIDUserInfoPayload
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.storage.TokenStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("LongParameterList")
internal class VKIDUserRefresher(
    private val api: VKIDApiService,
    private val tokenStorage: TokenStorage,
    private val stateGenerator: StateGenerator,
    private val deviceIdProvider: DeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val dispatchers: CoroutinesDispatchers,
    private val refresher: VKIDTokenRefresher,
) {
    suspend fun refresh(callback: VKIDGetUserCallback) {
        val accessToken = tokenStorage.accessToken?.token ?: run {
            callback.onFail(VKIDGetUserFail.NotAuthenticated("Not authorized"))
            return
        }
        val deviceId = deviceIdProvider.getDeviceId()
        val clientId = serviceCredentials.clientID
        val state = stateGenerator.regenerateState()
        val coroutineContext = currentCoroutineContext()
        withContext(dispatchers.io) {
            api.getUserInfo(
                accessToken = accessToken,
                clientId = clientId,
                deviceId = deviceId,
                state = state
            ).execute()
        }.onFailure {
            if (it is VKIDInvalidTokenException) {
                refresher.refresh(object : VKIDRefreshTokenCallback {
                    override fun onSuccess(token: AccessToken) {
                        CoroutineScope(coroutineContext + Job()).launch {
                            val call = withContext(dispatchers.io) {
                                api.getUserInfo(
                                    accessToken = token.token,
                                    clientId = clientId,
                                    deviceId = deviceId,
                                    state = state
                                ).execute()
                            }
                            withContext(dispatchers.main) {
                                call.onFailure {
                                    onFail(callback, it)
                                }.onSuccess {
                                    onSuccess(callback, it)
                                }
                            }
                        }
                    }

                    override fun onFail(fail: VKIDRefreshTokenFail) {
                        callback.onFail(VKIDGetUserFail.FailedApiCall("Failed to fetch user data due to ${it.message}", it))
                    }
                })
            } else {
                onFail(callback, it)
            }
        }.onSuccess { onSuccess(callback, it) }
    }

    private fun onFail(
        callback: VKIDGetUserCallback,
        throwable: Throwable,
    ) {
        callback.onFail(VKIDGetUserFail.FailedApiCall("Failed to fetch user data due to ${throwable.message}", throwable))
    }

    private fun onSuccess(
        callback: VKIDGetUserCallback,
        payload: VKIDUserInfoPayload,
    ) {
        callback.onSuccess(
            VKIDUser(
                firstName = payload.firstName,
                lastName = payload.lastName,
                photo200 = payload.avatar,
                phone = payload.phone,
                email = payload.email,
            )
        )
    }
}
