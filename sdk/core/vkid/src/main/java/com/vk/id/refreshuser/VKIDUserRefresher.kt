package com.vk.id.refreshuser

import com.vk.id.AccessToken
import com.vk.id.VKIDInvalidTokenException
import com.vk.id.VKIDUser
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refresh.VKIDRefreshTokenParams
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.storage.TokenStorage
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
    suspend fun refresh(
        callback: VKIDGetUserCallback,
        params: VKIDGetUserParams = VKIDGetUserParams {},
    ) {
        val accessToken = tokenStorage.accessToken?.token ?: run {
            callback.onFail(VKIDGetUserFail.NotAuthenticated("Not authorized"))
            return
        }
        val deviceId = deviceIdProvider.getDeviceId()
        val clientId = serviceCredentials.clientID
        val state = params.state ?: stateGenerator.regenerateState()
        withContext(dispatchers.io) {
            api.getUserInfo(
                accessToken = accessToken,
                clientId = clientId,
                deviceId = deviceId,
                state = state
            ).execute()
        }.onFailure {
            if (it is VKIDInvalidTokenException) {
                refresher.refresh(
                    callback = object : VKIDRefreshTokenCallback {
                        override fun onSuccess(token: AccessToken) {
                            callback.onSuccess(token.userData)
                        }

                        override fun onFail(fail: VKIDRefreshTokenFail) {
                            callback.onFail(VKIDGetUserFail.FailedApiCall("Failed to fetch user data due to ${it.message}", it))
                        }
                    },
                    params = VKIDRefreshTokenParams {
                        this.state = params.refreshTokenState
                        this.userFetchingState = state
                    }
                )
            } else {
                callback.onFail(VKIDGetUserFail.FailedApiCall("Failed to fetch user data due to ${it.message}", it))
            }
        }.onSuccess {
            callback.onSuccess(
                VKIDUser(
                    firstName = it.firstName,
                    lastName = it.lastName,
                    photo200 = it.avatar,
                    phone = it.phone,
                    email = it.email,
                )
            )
        }
    }
}
