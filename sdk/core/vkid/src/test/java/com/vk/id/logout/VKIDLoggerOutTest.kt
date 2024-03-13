package com.vk.id.logout

import android.content.Context
import com.vk.id.AccessToken
import com.vk.id.VKIDUser
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.api.VKIDCall
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.storage.TokenStorage
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

private const val CLIENT_ID = "client id"
private const val CLIENT_SECRET = "client secret"
private const val REDIRECT_URI = "redirect uri"
private const val ACCESS_TOKEN_VALUE = "access token"
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
    ACCESS_TOKEN_VALUE,
    USER_ID,
    -1,
    VKID_USER,
)

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class VKIDLoggerOutTest : BehaviorSpec({

    coroutineTestScope = true

    Given("Logger out") {
        val context = mockk<Context>()
        val api = mockk<VKIDApiService>()
        val tokenStorage = mockk<TokenStorage>()
        val deviceIdProvider = mockk<DeviceIdProvider>()
        every { deviceIdProvider.getDeviceId(context) } returns DEVICE_ID
        val serviceCredentials = ServiceCredentials(
            clientID = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            redirectUri = REDIRECT_URI,
        )
        val dispatchers = mockk<CoroutinesDispatchers>()
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        every { dispatchers.io } returns testDispatcher
        val loggerOut = VKIDLoggerOut(
            context = context,
            api = api,
            tokenStorage = tokenStorage,
            deviceIdProvider = deviceIdProvider,
            serviceCredentials = serviceCredentials,
            dispatchers = dispatchers,
        )
        When("Token is not available") {
            every { tokenStorage.accessToken } returns null
            every { tokenStorage.clear() } just runs
            val fail = VKIDLogoutFail.Unauthorized("Not authorized, can't logout")
            val callback = mockk<VKIDLogoutCallback>()
            every { callback.onFail(fail) } just runs
            runTest(scheduler) { loggerOut.logout(callback) }

            Then("Clears token storage") {
                verify { tokenStorage.clear() }
            }
            Then("Calls onFail with unauthorized fail") {
                verify(exactly = 0) { callback.onSuccess() }
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns an error") {
            every { tokenStorage.accessToken } returns ACCESS_TOKEN
            every { tokenStorage.clear() } just runs
            val call = mockk<VKIDCall<Unit>>()
            val exception = Exception("message")
            every { call.execute() } returns Result.failure(exception)
            coEvery {
                api.logout(
                    accessToken = ACCESS_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                )
            } returns call
            val fail = VKIDLogoutFail.FailedApiCall("Failed to logout due to message", exception)
            val callback = mockk<VKIDLogoutCallback>()
            every { callback.onFail(fail) } just runs
            runTest(scheduler) { loggerOut.logout(callback) }
            Then("Clears token storage") {
                verify { tokenStorage.clear() }
            }
            Then("Calls onFail with api fail") {
                verify(exactly = 0) { callback.onSuccess() }
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns invalid token exception") {
            every { tokenStorage.accessToken } returns ACCESS_TOKEN
            every { tokenStorage.clear() } just runs
            val call = mockk<VKIDCall<Unit>>()
            val exception = VKIDInvalidTokenException()
            every { call.execute() } returns Result.failure(exception)
            coEvery {
                api.logout(
                    accessToken = ACCESS_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                )
            } returns call
            val fail = VKIDLogoutFail.AccessTokenTokenExpired("Access token is expired, no need to logout")
            val callback = mockk<VKIDLogoutCallback>()
            every { callback.onFail(fail) } just runs
            runTest(scheduler) { loggerOut.logout(callback) }
            Then("Clears token storage") {
                verify { tokenStorage.clear() }
            }
            Then("Calls onFail with token expiration fail") {
                verify(exactly = 0) { callback.onSuccess() }
                verify { callback.onFail(fail) }
            }
        }
        When("Api call succeeds") {
            every { tokenStorage.accessToken } returns ACCESS_TOKEN
            every { tokenStorage.clear() } just runs
            val call = mockk<VKIDCall<Unit>>()
            every { call.execute() } returns Result.success(Unit)
            coEvery {
                api.logout(
                    accessToken = ACCESS_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                )
            } returns call
            val callback = mockk<VKIDLogoutCallback>()
            every { callback.onSuccess() } just runs
            runTest(scheduler) { loggerOut.logout(callback) }
            Then("Clears token storage") {
                verify { tokenStorage.clear() }
            }
            Then("Calls onSuccess") {
                verify { callback.onSuccess() }
                verify(exactly = 0) { callback.onFail(any()) }
            }
        }
    }
})
