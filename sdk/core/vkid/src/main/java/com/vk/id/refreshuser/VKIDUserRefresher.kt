package com.vk.id.refreshuser

import com.vk.id.VKIDUser
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
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
) {
    suspend fun refresh(callback: VKIDGetUserCallback) {
        val accessToken = tokenStorage.accessToken?.token ?: run {
            callback.onFail(VKIDGetUserFail.NotAuthenticated("Not authorized"))
            return
        }
        val deviceId = deviceIdProvider.getDeviceId()
        val clientId = serviceCredentials.clientID
        val state = stateGenerator.regenerateState()
        withContext(dispatchers.io) {
            api.getUserInfo(
                accessToken = accessToken,
                clientId = clientId,
                deviceId = deviceId,
                state = state
            ).execute()
        }.onFailure {
            callback.onFail(VKIDGetUserFail.FailedApiCall("Failed to fetch user data due to ${it.message}", it))
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
