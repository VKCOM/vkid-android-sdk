package com.vk.id.logout

import com.vk.id.VKIDInvalidTokenException
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.storage.TokenStorage
import kotlinx.coroutines.withContext

internal class VKIDLoggerOut(
    private val api: VKIDApiService,
    private val tokenStorage: TokenStorage,
    private val deviceIdProvider: DeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val dispatchers: CoroutinesDispatchers,
) {
    suspend fun logout(
        callback: VKIDLogoutCallback,
        @Suppress("UnusedParameter")
        params: VKIDLogoutParams = VKIDLogoutParams {},
    ) {
        val token = tokenStorage.accessToken?.token ?: run {
            tokenStorage.clear()
            callback.onFail(VKIDLogoutFail.NotAuthenticated("Not authorized, can't logout"))
            return
        }
        val deviceId = deviceIdProvider.getDeviceId()
        val clientId = serviceCredentials.clientID
        withContext(dispatchers.io) {
            api.logout(
                accessToken = token,
                deviceId = deviceId,
                clientId = clientId,
            ).execute()
        }.onFailure {
            tokenStorage.clear()
            if (it is VKIDInvalidTokenException) {
                callback.onFail(VKIDLogoutFail.AccessTokenTokenExpired("Access token is expired, no need to logout"))
            } else {
                callback.onFail(VKIDLogoutFail.FailedApiCall("Failed to logout due to ${it.message}", it))
            }
        }.onSuccess {
            tokenStorage.clear()
            callback.onSuccess()
        }
    }
}
