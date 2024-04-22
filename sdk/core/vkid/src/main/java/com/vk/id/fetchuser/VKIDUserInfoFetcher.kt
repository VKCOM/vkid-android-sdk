@file:OptIn(InternalVKIDApi::class)

package com.vk.id.fetchuser

import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.VKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import kotlinx.coroutines.withContext

internal class VKIDUserInfoFetcher(
    private val api: VKIDApiService,
    private val deviceIdProvider: VKIDDeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val dispatchers: VKIDCoroutinesDispatchers,
) {
    suspend fun fetch(
        accessToken: String,
        onSuccess: suspend (VKIDUser) -> Unit,
        onFailedApiCall: (Throwable) -> Unit,
    ) {
        val clientId = serviceCredentials.clientID
        val deviceId = withContext(dispatchers.io) { deviceIdProvider.getDeviceId() }
        val userInfoResult = withContext(dispatchers.io) {
            api.getUserInfo(
                accessToken = accessToken,
                clientId = clientId,
                deviceId = deviceId,
            ).execute()
        }
        userInfoResult.onFailure(onFailedApiCall)
        userInfoResult.onSuccess {
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
