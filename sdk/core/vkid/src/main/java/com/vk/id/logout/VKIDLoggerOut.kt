package com.vk.id.logout

import android.content.Context
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.storage.TokenStorage

internal class VKIDLoggerOut(
    private val context: Context,
    private val api: VKIDApiService,
    private val tokenStorage: TokenStorage,
    private val deviceIdProvider: DeviceIdProvider,
    private val serviceCredentials: ServiceCredentials,
) {
    fun logout(callback: VKIDLogoutCallback) {
        val token = tokenStorage.accessToken?.token ?: run {
            tokenStorage.clear()
            callback.onFail(VKIDLogoutFail.Unauthorized("Not authorized, can't logout"))
            return
        }
        val deviceId = deviceIdProvider.getDeviceId(context)
        val clientId = serviceCredentials.clientID
        api.logout(
            accessToken = token,
            deviceId = deviceId,
            clientId = clientId,
        ).execute().onFailure {
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
