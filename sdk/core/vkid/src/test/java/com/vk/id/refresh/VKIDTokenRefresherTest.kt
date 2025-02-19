@file:OptIn(InternalVKIDApi::class)

package com.vk.id.refresh

import com.vk.id.AccessToken
import com.vk.id.RefreshToken
import com.vk.id.TokensHandler
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.network.InternalVKIDCall
import com.vk.id.storage.TokenStorage
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
private const val REFRESH_TOKEN_VALUE = "refresh token"
private const val ID_TOKEN = "id token"
private const val DEVICE_ID = "device id"
private const val STATE = "state"
private const val PARAMS_STATE = "parms state"
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
    idToken = ID_TOKEN,
    userID = USER_ID,
    expireTime = -1,
    userData = VKID_USER,
    scopes = setOf("phone", "email"),
)
private val REFRESH_TOKEN = RefreshToken(
    token = REFRESH_TOKEN_VALUE,
    scopes = setOf("phone", "email"),
)
private val TOKEN_PAYLOAD = VKIDTokenPayload(
    accessToken = ACCESS_TOKEN_VALUE,
    refreshToken = REFRESH_TOKEN_VALUE,
    idToken = ID_TOKEN,
    expiresIn = -1,
    userId = USER_ID,
    state = STATE,
    scope = "phone email",
)

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class VKIDTokenRefresherTest : BehaviorSpec({

    coroutineTestScope = true

    Given("Token refresher") {
        val api = mockk<VKIDApiService>()
        val deviceIdProvider = mockk<InternalVKIDDeviceIdProvider>()
        every { deviceIdProvider.getDeviceId() } returns DEVICE_ID
        val serviceCredentials = ServiceCredentials(
            clientID = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            redirectUri = REDIRECT_URI,
        )
        val stateGenerator = mockk<StateGenerator>()
        val tokensHandler = mockk<TokensHandler>()
        val dispatchers = mockk<VKIDCoroutinesDispatchers>()
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        every { dispatchers.io } returns testDispatcher
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val tokenStorage = mockk<TokenStorage>()
        val refresher = VKIDTokenRefresher(
            api = api,
            deviceIdProvider = deviceIdProvider,
            serviceCredentials = serviceCredentials,
            stateGenerator = stateGenerator,
            tokensHandler = tokensHandler,
            dispatchers = dispatchers,
            prefsStore = prefsStore,
            tokenStorage = tokenStorage,
        )
        every { stateGenerator.regenerateState() } returns STATE
        every { deviceIdProvider.getDeviceId() } returns DEVICE_ID
        When("Refresh token is missing") {
            every { tokenStorage.refreshToken } returns null
            val callback = mockk<VKIDRefreshTokenCallback>()
            val fail = VKIDRefreshTokenFail.NotAuthenticated("You must login before refreshing token")
            every { callback.onFail(fail) } just runs
            runTest(scheduler) {
                refresher.refresh(callback)
            }
            Then("Emits not authenticated fail") {
                verify { callback.onFail(fail) }
            }
        }
        When("Token refresh fails") {
            every { tokenStorage.refreshToken } returns REFRESH_TOKEN
            val call = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            val exception = Exception("message")
            every { call.execute() } returns Result.failure(exception)
            every {
                api.refreshToken(
                    refreshToken = REFRESH_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = PARAMS_STATE,
                )
            } returns call
            val callback = mockk<VKIDRefreshTokenCallback>()
            val fail = VKIDRefreshTokenFail.FailedApiCall("Failed code to refresh token due to: message", exception)
            every { callback.onFail(fail) } just runs
            every { prefsStore.clear() } just runs
            runTest(scheduler) {
                refresher.refresh(
                    callback,
                    params = VKIDRefreshTokenParams {
                        state = PARAMS_STATE
                    }
                )
            }
            Then("Clears prefs") {
                verify { prefsStore.clear() }
            }
            Then("Emits not authenticated fail") {
                verify { callback.onFail(fail) }
            }
        }
        When("Api call returns the wrong state") {
            every { tokenStorage.refreshToken } returns REFRESH_TOKEN
            val call = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            every { call.execute() } returns Result.success(TOKEN_PAYLOAD.copy(state = "wrong state"))
            every {
                api.refreshToken(
                    refreshToken = REFRESH_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = STATE,
                )
            } returns call
            val callback = mockk<VKIDRefreshTokenCallback>()
            val fail = VKIDRefreshTokenFail.FailedOAuthState("Wrong state for getting user info")
            every { callback.onFail(fail) } just runs
            every { prefsStore.clear() } just runs
            runTest(scheduler) {
                refresher.refresh(callback)
            }
            Then("Clears prefs") {
                verify { prefsStore.clear() }
            }
            Then("Emits wrong state fail") {
                verify { callback.onFail(fail) }
            }
        }
        When("Token handler fails with api call error") {
            every { tokenStorage.refreshToken } returns REFRESH_TOKEN
            val call = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            every { call.execute() } returns Result.success(TOKEN_PAYLOAD)
            every {
                api.refreshToken(
                    refreshToken = REFRESH_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = STATE,
                )
            } returns call
            val onFailedApiCall = slot<(Throwable) -> Unit>()
            val onSuccess = slot<suspend (AccessToken) -> Unit>()
            coEvery {
                tokensHandler.handle(
                    payload = TOKEN_PAYLOAD,
                    onSuccess = capture(onSuccess),
                    onFailedApiCall = capture(onFailedApiCall),
                    refreshAccessToken = false
                )
            } just runs
            val callback = mockk<VKIDRefreshTokenCallback>()
            val exception = Exception("message")
            val fail = VKIDRefreshTokenFail.FailedApiCall("Failed to fetch user data due to: message", exception)
            every { callback.onFail(fail) } just runs
            every { prefsStore.clear() } just runs
            runTest(scheduler) {
                refresher.refresh(callback, params = VKIDRefreshTokenParams { refreshAccessToken = false })
            }
            Then("Clears prefs") {
                verify { prefsStore.clear() }
            }
            Then("Calls callback's onFail") {
                onFailedApiCall.captured(exception)
                verify { callback.onFail(fail) }
            }
        }
        When("Token handling succeeds") {
            every { tokenStorage.refreshToken } returns REFRESH_TOKEN
            val call = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            every { call.execute() } returns Result.success(TOKEN_PAYLOAD)
            every {
                api.refreshToken(
                    refreshToken = REFRESH_TOKEN_VALUE,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = STATE,
                )
            } returns call
            val onFailedApiCall = slot<(Throwable) -> Unit>()
            val onSuccess = slot<suspend (AccessToken) -> Unit>()
            coEvery {
                tokensHandler.handle(
                    payload = TOKEN_PAYLOAD,
                    onSuccess = capture(onSuccess),
                    onFailedApiCall = capture(onFailedApiCall),
                    refreshAccessToken = false
                )
            } just runs
            val callback = mockk<VKIDRefreshTokenCallback>()
            every { callback.onSuccess(ACCESS_TOKEN) } just runs
            every { prefsStore.clear() } just runs
            runTest(scheduler) {
                refresher.refresh(callback, params = VKIDRefreshTokenParams { refreshAccessToken = false })
            }
            Then("Clears prefs") {
                verify { prefsStore.clear() }
            }
            Then("Calls callback's onFail") {
                onSuccess.captured(ACCESS_TOKEN)
                verify { callback.onSuccess(ACCESS_TOKEN) }
            }
        }
    }
})
