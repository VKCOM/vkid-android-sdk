@file:OptIn(InternalVKIDApi::class)

package com.vk.id.refreshuser

import com.vk.id.AccessToken
import com.vk.id.VKIDInvalidTokenException
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.api.dto.VKIDUserInfoPayload
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.network.VKIDCall
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.storage.TokenStorage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

private const val CLIENT_ID = "client id"
private const val CLIENT_SECRET = "client secret"
private const val REDIRECT_URI = "redirect uri"
private const val ACCESS_TOKEN_VALUE = "access token"
private const val ID_TOKEN_VALUE = "id token"
private const val DEVICE_ID = "device id"
private const val FIRST_NAME = "first"
private const val LAST_NAME = "last"
private const val PHONE = "phone"
private const val AVATAR = "avatar"
private const val EMAIL = "email"
private const val USER_ID = 100L
private val VKID_USER = VKIDUser(
    firstName = FIRST_NAME,
    lastName = LAST_NAME,
    phone = PHONE,
    photo50 = null,
    photo100 = null,
    photo200 = AVATAR,
    email = EMAIL,
)
private val ACCESS_TOKEN = AccessToken(
    token = ACCESS_TOKEN_VALUE,
    idToken = ID_TOKEN_VALUE,
    userID = USER_ID,
    expireTime = -1,
    userData = VKID_USER,
)
private val USER_INFO_PAYLOAD = VKIDUserInfoPayload(
    firstName = FIRST_NAME,
    lastName = LAST_NAME,
    phone = PHONE,
    email = EMAIL,
    avatar = AVATAR,
)

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class VKIDUserRefresherTest : BehaviorSpec({

    coroutineTestScope = true
    isolationMode = IsolationMode.InstancePerLeaf

    Given("User info fetcher") {
        val api = mockk<VKIDApiService>()
        val tokenStorage = mockk<TokenStorage>()
        val deviceIdProvider = mockk<DeviceIdProvider>()
        every { deviceIdProvider.getDeviceId() } returns DEVICE_ID
        val serviceCredentials = ServiceCredentials(
            clientID = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            redirectUri = REDIRECT_URI,
        )
        val dispatchers = mockk<CoroutinesDispatchers>()
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        every { dispatchers.io } returns testDispatcher
        val tokenRefresher = mockk<VKIDTokenRefresher>()
        val refresher = VKIDUserRefresher(
            api = api,
            tokenStorage = tokenStorage,
            deviceIdProvider = deviceIdProvider,
            serviceCredentials = serviceCredentials,
            dispatchers = dispatchers,
            refresher = tokenRefresher,
        )
        When("Token is not available") {
            every { tokenStorage.accessToken } returns null
            val fail = VKIDGetUserFail.NotAuthenticated("Not authorized")
            val callback = mockk<VKIDGetUserCallback>()
            every { callback.onFail(fail) } just runs
            runTest(scheduler) { refresher.refresh(callback) }
            Then("Calls onFail with unauthorized fail") {
                verify(exactly = 0) { callback.onSuccess(any()) }
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns an error") {
            every { tokenStorage.accessToken } returns ACCESS_TOKEN
            val call = mockk<VKIDCall<VKIDUserInfoPayload>>()
            val exception = Exception("message")
            every { call.execute() } returns Result.failure(exception)
            coEvery {
                api.getUserInfo(
                    accessToken = ACCESS_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                )
            } returns call
            val fail = VKIDGetUserFail.FailedApiCall("Failed to fetch user data due to message", exception)
            val callback = mockk<VKIDGetUserCallback>()
            every { callback.onFail(fail) } just runs
            runTest(scheduler) { refresher.refresh(callback) }
            Then("Calls onFail with api fail") {
                verify(exactly = 0) { callback.onSuccess(any()) }
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns invalid token error and refresh fails") {
            every { tokenStorage.accessToken } returns ACCESS_TOKEN
            val call = mockk<VKIDCall<VKIDUserInfoPayload>>()
            val exception = VKIDInvalidTokenException()
            every { call.execute() } returns Result.failure(exception)
            coEvery {
                api.getUserInfo(
                    accessToken = ACCESS_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                )
            } returns call
            val refreshTokenCallback = slot<VKIDRefreshTokenCallback>()
            coEvery { tokenRefresher.refresh(capture(refreshTokenCallback), any()) } just runs
            val fail = VKIDGetUserFail.FailedApiCall("Failed to fetch user data due to null", exception)
            val callback = mockk<VKIDGetUserCallback>()
            every { callback.onFail(fail) } just runs
            runTest(scheduler) { refresher.refresh(callback) }
            refreshTokenCallback.captured.onFail(VKIDRefreshTokenFail.FailedApiCall(fail.description, fail.throwable))
            scheduler.advanceUntilIdle()
            Then("Calls onFail with api fail") {
                verify(exactly = 0) { callback.onSuccess(any()) }
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns invalid token error and refresh succeeds") {
            every { tokenStorage.accessToken } returns ACCESS_TOKEN
            val call = mockk<VKIDCall<VKIDUserInfoPayload>>()
            val exception = VKIDInvalidTokenException()
            every { call.execute() } returns Result.failure(exception)
            coEvery {
                api.getUserInfo(
                    accessToken = ACCESS_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                )
            } returns call
            val refreshTokenCallback = slot<VKIDRefreshTokenCallback>()
            coEvery { tokenRefresher.refresh(capture(refreshTokenCallback), any()) } just runs
            val callback = mockk<VKIDGetUserCallback>()
            every { callback.onSuccess(VKID_USER) } just runs
            runTest(scheduler) { refresher.refresh(callback) }
            refreshTokenCallback.captured.onSuccess(ACCESS_TOKEN)
            scheduler.advanceUntilIdle()
            Then("Calls onFail with api fail") {
                verify { callback.onSuccess(VKID_USER) }
                verify(exactly = 0) { callback.onFail(any()) }
            }
        }
        When("Api returns user") {
            every { tokenStorage.accessToken } returns ACCESS_TOKEN
            val call = mockk<VKIDCall<VKIDUserInfoPayload>>()
            every { call.execute() } returns Result.success(USER_INFO_PAYLOAD)
            coEvery {
                api.getUserInfo(
                    accessToken = ACCESS_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                )
            } returns call
            val callback = mockk<VKIDGetUserCallback>()
            every { callback.onSuccess(VKID_USER) } just runs
            runTest(scheduler) { refresher.refresh(callback) }
            Then("Calls onSuccess") {
                verify { callback.onSuccess(VKID_USER) }
                verify(exactly = 0) { callback.onFail(any()) }
            }
        }
    }
})
