package com.vk.id.fetchuser

import com.vk.id.VKIDUser
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import kotlinx.coroutines.withContext

internal class VKIDUserInfoFetcher(
    private val api: VKIDApiService,
    private val stateGenerator: StateGenerator,
    private val deviceIdProvider: DeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val dispatchers: CoroutinesDispatchers,
) {
    suspend fun fetch(
        accessToken: String,
        onSuccess: (VKIDUser) -> Unit,
        onFailedApiCall: (Throwable) -> Unit,
        onFailedOAuthState: () -> Unit,
    ) {
        val clientId = serviceCredentials.clientID
        val deviceId = deviceIdProvider.getDeviceId()
        val userInfoState = stateGenerator.regenerateState()
        val userInfoResult = withContext(dispatchers.io) {
            api.getUserInfo(
                accessToken = accessToken,
                clientId = clientId,
                deviceId = deviceId,
                state = userInfoState
            ).execute()
        }
        userInfoResult.onFailure(onFailedApiCall)
        userInfoResult.onSuccess {
            if (it.state != userInfoState) {
                onFailedOAuthState()
                return
            }
            onSuccess(
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
