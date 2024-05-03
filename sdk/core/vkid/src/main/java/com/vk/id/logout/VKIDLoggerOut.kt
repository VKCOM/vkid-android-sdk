@file:OptIn(InternalVKIDApi::class)

package com.vk.id.logout

import com.vk.id.VKIDInvalidTokenException
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.storage.TokenStorage
import kotlinx.coroutines.withContext

internal class VKIDLoggerOut(
    private val api: VKIDApiService,
    private val tokenStorage: TokenStorage,
    private val deviceIdProvider: InternalVKIDDeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
    private val dispatchers: VKIDCoroutinesDispatchers,
) {
    suspend fun logout(
        callback: VKIDLogoutCallback,
        accessToken: String? = null,
        clearTokenStorage: Boolean = true,
        @Suppress("UnusedParameter")
        params: VKIDLogoutParams = VKIDLogoutParams {},
    ) {
        val (token, deviceId) = withContext(dispatchers.io) {
            (accessToken ?: tokenStorage.accessToken?.token) to deviceIdProvider.getDeviceId()
        }
        token ?: run {
            if (clearTokenStorage) withContext(dispatchers.io) { tokenStorage.clear() }
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
            if (it is VKIDInvalidTokenException) {
                if (clearTokenStorage) withContext(dispatchers.io) { tokenStorage.clear() }
                callback.onSuccess()
            } else {
                callback.onFail(VKIDLogoutFail.FailedApiCall("Failed to logout due to ${it.message}", it))
            }
        }.onSuccess {
            if (clearTokenStorage) withContext(dispatchers.io) { tokenStorage.clear() }
            callback.onSuccess()
        }
    }
}
