@file:OptIn(InternalVKIDApi::class)

package com.vk.id.logout

import com.vk.id.AccessToken
import com.vk.id.VKIDInvalidTokenException
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.VKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.network.VKIDCall
import com.vk.id.storage.TokenStorage
import io.kotest.core.spec.IsolationMode
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

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class VKIDLoggerOutTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    coroutineTestScope = true

    Given("Logger out") {
        val api = mockk<VKIDApiService>()
        val tokenStorage = mockk<TokenStorage>()
        val deviceIdProvider = mockk<VKIDDeviceIdProvider>()
        every { deviceIdProvider.getDeviceId() } returns DEVICE_ID
        val serviceCredentials = ServiceCredentials(
            clientID = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            redirectUri = REDIRECT_URI,
        )
        val dispatchers = mockk<VKIDCoroutinesDispatchers>()
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        every { dispatchers.io } returns testDispatcher
        val loggerOut = VKIDLoggerOut(
            api = api,
            tokenStorage = tokenStorage,
            deviceIdProvider = deviceIdProvider,
            serviceCredentials = serviceCredentials,
            dispatchers = dispatchers,
        )
        When("Token is not available") {
            And("Requests to clear token storage") {
                val clearTokenStorage = true
                every { tokenStorage.accessToken } returns null
                every { tokenStorage.clear() } just runs
                val fail = VKIDLogoutFail.NotAuthenticated("Not authorized, can't logout")
                val callback = mockk<VKIDLogoutCallback>()
                every { callback.onFail(fail) } just runs
                runTest(scheduler) { loggerOut.logout(callback, clearTokenStorage = clearTokenStorage) }

                Then("Clears token storage") {
                    verify { tokenStorage.clear() }
                }
                Then("Calls onFail with unauthorized fail") {
                    verify(exactly = 0) { callback.onSuccess() }
                    verify { callback.onFail(fail) }
                }
            }
        }
        When("Token is not available") {
            And("Requests to not clear token storage") {
                val clearTokenStorage = false
                val accessToken = null
                every { tokenStorage.accessToken } returns null
                val fail = VKIDLogoutFail.NotAuthenticated("Not authorized, can't logout")
                val callback = mockk<VKIDLogoutCallback>()
                every { callback.onFail(fail) } just runs
                runTest(scheduler) { loggerOut.logout(callback, clearTokenStorage = clearTokenStorage, accessToken = accessToken) }

                Then("Doesn't clear token storage") {
                    verify(exactly = 0) { tokenStorage.clear() }
                }
                Then("Calls onFail with unauthorized fail") {
                    verify(exactly = 0) { callback.onSuccess() }
                    verify { callback.onFail(fail) }
                }
            }
        }
        When("Api returns an error") {
            And("Requests to clear token storage") {
                val clearTokenStorage = true
                every { tokenStorage.accessToken } returns ACCESS_TOKEN
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
                runTest(scheduler) { loggerOut.logout(callback, clearTokenStorage = clearTokenStorage) }
                Then("Calls onFail with api fail") {
                    verify(exactly = 0) { callback.onSuccess() }
                    verify { callback.onFail(fail) }
                }
            }
        }
        When("Api returns an error") {
            And("Requests to not clear token storage") {
                val clearTokenStorage = false
                val accessToken = ACCESS_TOKEN_VALUE
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
                runTest(scheduler) { loggerOut.logout(callback, clearTokenStorage = clearTokenStorage, accessToken = accessToken) }
                Then("Doesn't clear token storage") {
                    verify(exactly = 0) { tokenStorage.clear() }
                }
                Then("Calls onFail with api fail") {
                    verify(exactly = 0) { callback.onSuccess() }
                    verify { callback.onFail(fail) }
                }
            }
        }
        When("Api returns invalid token exception") {
            And("Requests to clear token storage") {
                val clearTokenStorage = true
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
                val callback = mockk<VKIDLogoutCallback>()
                every { callback.onSuccess() } just runs
                runTest(scheduler) {
                    loggerOut.logout(
                        callback = callback,
                        clearTokenStorage = clearTokenStorage,
                    )
                }
                And("Refreshing tokens fails") {
                    Then("Clears token storage") {
                        verify { tokenStorage.clear() }
                    }
                    Then("Calls onSuccess") {
                        verify { callback.onSuccess() }
                        verify(exactly = 0) { callback.onFail(any()) }
                    }
                }
            }
        }
        When("Api returns invalid token exception") {
            And("Requests to not clear token storage") {
                val clearTokenStorage = false
                val accessToken = ACCESS_TOKEN_VALUE
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
                val callback = mockk<VKIDLogoutCallback>()
                every { callback.onSuccess() } just runs
                runTest(scheduler) {
                    loggerOut.logout(
                        callback = callback,
                        clearTokenStorage = clearTokenStorage,
                        accessToken = accessToken,
                    )
                }
                And("Refreshing tokens fails") {
                    Then("Doesn't clear token storage") {
                        verify(exactly = 0) { tokenStorage.clear() }
                    }
                    Then("Calls onSuccess") {
                        verify { callback.onSuccess() }
                        verify(exactly = 0) { callback.onFail(any()) }
                    }
                }
            }
        }
        When("Api call succeeds") {
            And("Requests to clear token storage") {
                val clearTokenStorage = true
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
                runTest(scheduler) { loggerOut.logout(callback = callback, clearTokenStorage = clearTokenStorage) }
                Then("Clears token storage") {
                    verify { tokenStorage.clear() }
                }
                Then("Calls onSuccess") {
                    verify { callback.onSuccess() }
                    verify(exactly = 0) { callback.onFail(any()) }
                }
            }
        }
        When("Api call succeeds") {
            And("Requests to not clear token storage") {
                val clearTokenStorage = false
                val accessToken = ACCESS_TOKEN_VALUE
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
                runTest(scheduler) { loggerOut.logout(callback = callback, clearTokenStorage = clearTokenStorage, accessToken = accessToken) }
                Then("Doesn't clear token storage") {
                    verify(exactly = 0) { tokenStorage.clear() }
                }
                Then("Calls onSuccess") {
                    verify { callback.onSuccess() }
                    verify(exactly = 0) { callback.onFail(any()) }
                }
            }
        }
    }
})
