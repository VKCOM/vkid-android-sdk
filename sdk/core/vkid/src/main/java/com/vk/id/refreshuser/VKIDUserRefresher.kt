@file:OptIn(InternalVKIDApi::class)

package com.vk.id.refreshuser

import com.vk.id.AccessToken
import com.vk.id.VKIDInvalidTokenException
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.VKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
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
    private val deviceIdProvider: VKIDDeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val dispatchers: VKIDCoroutinesDispatchers,
    private val refresher: VKIDTokenRefresher,
) {
    suspend fun refresh(
        callback: VKIDGetUserCallback,
        params: VKIDGetUserParams = VKIDGetUserParams {},
    ) {
        val clientId = serviceCredentials.clientID
        val (accessToken, deviceId) = withContext(dispatchers.io) {
            tokenStorage.accessToken?.token to deviceIdProvider.getDeviceId()
        }
        accessToken ?: run {
            callback.onFail(VKIDGetUserFail.NotAuthenticated("Not authorized"))
            return
        }
        withContext(dispatchers.io) {
            api.getUserInfo(
                accessToken = accessToken,
                clientId = clientId,
                deviceId = deviceId,
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
