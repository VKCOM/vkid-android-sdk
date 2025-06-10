@file:OptIn(InternalVKIDApi::class)

package com.vk.id.vksdksupport

import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.exceptions.VKApiCodes
import com.vk.api.sdk.exceptions.VKApiExecutionException
import com.vk.api.sdk.internal.ApiCommand
import com.vk.dto.common.id.UserId
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refresh.VKIDRefreshTokenParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingQueue

/**
 * Add compatibility for vk-android-sdk with vkid-android-sdk to the request.
 *
 * This adds two things:
 * - Automatically uses VKID access token for the request
 * - Refreshes access token if necessary
 *
 * This extension adds two exceptions as the possible outcomes:
 * - [VKIDNotAuthorizedException] if the user wasn't authorized before executing the command
 * - [VKIDRefreshTokenFailException] if the SDK failed to refresh token
 *
 * @param refreshTokenParamsProvider Providers parameters for token refreshing.
 * This can be useful if you want to generate PKCE parameters yourself.
 *
 * @since 1.3.2
 */
public fun <T> ApiCommand<T>.withVKIDToken(
    refreshTokenParamsProvider: () -> VKIDRefreshTokenParams = { VKIDRefreshTokenParams {} }
): ApiCommand<T> {
    return object : ApiCommand<T>() {

        private fun saveToken(token: AccessToken) {
            VK.saveAccessToken(
                userId = UserId(token.userID),
                accessToken = token.token,
                secret = null,
                expiresInSec = ((System.currentTimeMillis() - token.expireTime) / 1000).toInt(),
                createdMs = System.currentTimeMillis(),
            )
        }

        override fun onExecute(manager: VKApiManager): T {
            return VKID.instance.crashReporter.runReportingCrashes({ throw it }) { onExecuteInternal(manager) }
        }

        private fun onExecuteInternal(manager: VKApiManager): T {
            try {
                saveToken(VKID.instance.accessToken ?: throw VKIDNotAuthorizedException())
                return this@withVKIDToken.execute(manager)
            } catch (t: VKApiExecutionException) {
                if (t.code == VKApiCodes.CODE_AUTHORIZATION_FAILED) {
                    val result = LinkedBlockingQueue<Result<T>>(1)
                    CoroutineScope(Dispatchers.IO).launch {
                        VKID.instance.refreshToken(
                            object : VKIDRefreshTokenCallback {
                                override fun onSuccess(token: AccessToken) {
                                    saveToken(token)
                                    try {
                                        result.offer(Result.success(this@withVKIDToken.execute(manager)))
                                    } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
                                        result.offer(Result.failure(t))
                                    }
                                }

                                override fun onFail(fail: VKIDRefreshTokenFail) {
                                    result.offer(Result.failure(VKIDRefreshTokenFailException(fail)))
                                }
                            },
                            params = refreshTokenParamsProvider()
                        )
                    }
                    return result.take().getOrThrow()
                } else {
                    throw t
                }
            }
        }
    }
}
