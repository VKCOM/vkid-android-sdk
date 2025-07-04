@file:OptIn(InternalVKIDApi::class)

package com.vk.id

import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.logout.VKIDLoggerOut
import com.vk.id.logout.VKIDLogoutCallback
import com.vk.id.logout.VKIDLogoutFail
import com.vk.id.logout.VKIDLogoutParams
import com.vk.id.network.InternalVKIDCall
import com.vk.id.storage.InternalVKIDTokenStorage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecGivenContainerScope
import io.kotest.core.test.testCoroutineScheduler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

private const val ERROR_MESSAGE = "Error message"
private val error = IllegalStateException("Error")
private const val ACCESS_TOKEN_VALUE = "access token"
private const val REFRESH_TOKEN = "refresh token"
private const val ID_TOKEN = "id token"
private const val USER_ID = 0L
private const val STATE = "state"
private const val SCOPE = "phone email"
private const val DIFFERENT_STATE = "differentstate"
private const val CODE_VERIFIER = "code verifier"
private const val CODE = "code"
private const val DEVICE_ID = "device id"
private const val CLIENT_ID = "client id"
private const val CLIENT_SECRET = "client secret"
private const val REDIRECT_URI = "redirect uri"
private val authResultSuccess = AuthResult.Success(
    oauth = AuthResult.OAuth(CODE, STATE),
    deviceId = DEVICE_ID
)
private val ACCESS_TOKEN = AccessToken(
    token = ACCESS_TOKEN_VALUE,
    idToken = ID_TOKEN,
    userID = USER_ID,
    expireTime = -1,
    userData = VKIDUser(
        firstName = "first",
        lastName = "last",
        phone = "phone",
        photo50 = "50",
        photo100 = "100",
        photo200 = "200",
        email = "email"
    ),
    scopes = setOf("phone", "email"),
)
private val AUTH_CODE_DATA = AuthCodeData(code = CODE, deviceId = DEVICE_ID)

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class AuthResultHandlerTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    coroutineTestScope = true

    Given("An AuthResultHandler") {
        val callbacksHolder = mockk<AuthCallbacksHolder>()
        val deviceIdProvider = mockk<InternalVKIDDeviceIdProvider>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        val dispatchers = mockk<VKIDCoroutinesDispatchers>()
        every { dispatchers.io } returns testDispatcher
        val api = mockk<VKIDApiService>()
        val serviceCredentials = mockk<ServiceCredentials>()
        val tokensHandler = mockk<TokensHandler>()
        val loggerOut = mockk<VKIDLoggerOut>()
        val tokenStorage = mockk<InternalVKIDTokenStorage>()
        val onFailCallback = mockk<() -> Unit>()
        every { onFailCallback() } just runs
        val handler = AuthResultHandler(
            dispatchers = dispatchers,
            callbacksHolder = callbacksHolder,
            deviceIdProvider = deviceIdProvider,
            prefsStore = prefsStore,
            serviceCredentials = serviceCredentials,
            api = api,
            tokensHandler = tokensHandler,
            loggerOut = loggerOut,
            tokenStorage = tokenStorage,
        )

        suspend fun BehaviorSpecGivenContainerScope.whenHandleIsCalledWithFail(
            authResult: AuthResult,
            authFail: VKIDAuthFail,
            onFail: () -> Unit
        ) = When("Handle is called with ${authResult::class.simpleName}") {
            val callback = mockk<VKIDAuthCallback>()
            every { callbacksHolder.isEmpty() } returns false
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onFail(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { prefsStore.clear() } just runs
            runTest(scheduler) {
                handler.handle(authResult, onFail)
            }
            Then("Checks whether callbacks are present") {
                verify { callbacksHolder.isEmpty() }
            }
            Then("state is cleared") {
                verify { prefsStore.clear() }
            }
            Then("Callbacks are requested from holder") {
                verify { callbacksHolder.getAll() }
            }
            Then("It is emitted") {
                verify { callback.onFail(authFail) }
            }
            Then("Callbacks holder is cleared") {
                verify { callbacksHolder.clear() }
            }
            Then("OnFail is called") {
                verify(exactly = 1) { onFail() }
            }
        }
        whenHandleIsCalledWithFail(
            AuthResult.Canceled(ERROR_MESSAGE),
            VKIDAuthFail.Canceled(ERROR_MESSAGE),
            onFailCallback
        )
        whenHandleIsCalledWithFail(
            AuthResult.NoBrowserAvailable(ERROR_MESSAGE, error),
            VKIDAuthFail.NoBrowserAvailable(ERROR_MESSAGE, error),
            onFailCallback
        )
        whenHandleIsCalledWithFail(
            AuthResult.AuthActiviyResultFailed(ERROR_MESSAGE, error),
            VKIDAuthFail.FailedRedirectActivity(ERROR_MESSAGE, error),
            onFailCallback
        )

        When("There are not callbacks") {
            val authResult = authResultSuccess.copy(oauth = null)
            every { callbacksHolder.isEmpty() } returns true
            runTest(scheduler) {
                handler.handle(authResult, onFailCallback)
            }
            Then("Checks whether callbacks are present") {
                verify { callbacksHolder.isEmpty() }
            }
            Then("Callbacks are not accessed") {
                verify(exactly = 0) { callbacksHolder.getAll() }
            }
            Then("OnFail callback not called") {
                verify(exactly = 0) { onFailCallback() }
            }
        }

        When("Auth result doesn't have oAuth") {
            val authResult = authResultSuccess.copy(oauth = null)
            val callback = mockk<VKIDAuthCallback>()
            every { callbacksHolder.isEmpty() } returns false
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onFail(any()) } just runs
            every { callbacksHolder.clear() } just runs
            runTest(scheduler) {
                handler.handle(authResult, onFailCallback)
            }
            Then("Checks whether callbacks are present") {
                verify { callbacksHolder.isEmpty() }
            }
            Then("Callbacks are requested from holder") {
                verify { callbacksHolder.getAll() }
            }
            Then("It is emitted") {
                verify {
                    callback.onFail(ofType(VKIDAuthFail.FailedOAuth::class))
                }
            }
            Then("Callbacks holder is cleared") {
                verify { callbacksHolder.clear() }
            }
            Then("OnFail callback called") {
                verify(exactly = 1) { onFailCallback() }
            }
        }

        When("Handle is called with wrong state") {
            val authResult = authResultSuccess
            val callback = mockk<VKIDAuthCallback>()
            every { callbacksHolder.isEmpty() } returns false
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onFail(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            every { prefsStore.state } returns DIFFERENT_STATE
            every { prefsStore.clear() } just runs
            every { prefsStore.codeVerifier } returns CODE_VERIFIER
            runTest(scheduler) {
                handler.handle(authResult, onFailCallback)
            }
            scheduler.advanceUntilIdle()
            Then("Checks whether callbacks are present") {
                verify { callbacksHolder.isEmpty() }
            }
            Then("state is cleared") {
                verify { prefsStore.clear() }
            }
            Then("Device id is saved") {
                verify { deviceIdProvider.setDeviceId(DEVICE_ID) }
            }
            Then("Callbacks are requested from holder") {
                verify { callbacksHolder.getAll() }
            }
            Then("It is emitted") {
                verify { callback.onFail(VKIDAuthFail.FailedOAuthState("Invalid state")) }
            }
            Then("Callbacks holder is cleared") {
                verify { callbacksHolder.clear() }
            }
            Then("OnFail callback called") {
                verify(exactly = 1) { onFailCallback() }
            }
        }
        When("Handle is called and api returns an error") {
            val authResult = authResultSuccess
            val callback = mockk<VKIDAuthCallback>()
            val call = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            every { callbacksHolder.isEmpty() } returns false
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onAuthCode(any(), any()) } just runs
            every { callback.onFail(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            every { prefsStore.state } returns STATE
            every { prefsStore.clear() } just runs
            every { prefsStore.codeVerifier } returns CODE_VERIFIER
            every { serviceCredentials.clientID } returns CLIENT_ID
            every { serviceCredentials.clientSecret } returns CLIENT_SECRET
            every { serviceCredentials.redirectUri } returns REDIRECT_URI
            every { api.getToken(CODE, CODE_VERIFIER, CLIENT_ID, DEVICE_ID, REDIRECT_URI, STATE) } returns call
            every { call.execute() } returns Result.failure(error)
            every { tokenStorage.accessToken } returns null
            runTest(scheduler) {
                handler.handle(authResult, onFailCallback)
            }
            scheduler.advanceUntilIdle()
            Then("Checks whether callbacks are present") {
                verify { callbacksHolder.isEmpty() }
            }
            Then("Device id is saved") {
                verify { deviceIdProvider.setDeviceId(DEVICE_ID) }
            }
            Then("state is cleared") {
                verify { prefsStore.clear() }
            }
            Then("Callbacks are requested from holder") {
                verify { callbacksHolder.getAll() }
            }
            Then("Auth code is emitted") {
                verify { callback.onAuthCode(AUTH_CODE_DATA, false) }
            }
            Then("It is emitted") {
                verify {
                    callback.onFail(
                        VKIDAuthFail.FailedApiCall("Failed code to token exchange api call: ${error.message}", error)
                    )
                }
            }
            Then("Callbacks holder is cleared") {
                verify { callbacksHolder.clear() }
            }
            Then("OnFail callback called") {
                verify(exactly = 1) { onFailCallback() }
            }
        }
        When("Handle is called and api returns success") {
            val authResult = authResultSuccess
            val callback = mockk<VKIDAuthCallback>()
            val call = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            val payload = VKIDTokenPayload(ACCESS_TOKEN_VALUE, REFRESH_TOKEN, ID_TOKEN, 0, USER_ID, STATE, SCOPE)
            every { callbacksHolder.isEmpty() } returns false
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onAuthCode(any(), any()) } just runs
            every { callback.onAuth(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            every { prefsStore.state } returns STATE
            every { prefsStore.clear() } just runs
            every { prefsStore.codeVerifier } returns CODE_VERIFIER
            every { serviceCredentials.clientID } returns CLIENT_ID
            every { serviceCredentials.clientSecret } returns CLIENT_SECRET
            every { serviceCredentials.redirectUri } returns REDIRECT_URI
            every { api.getToken(CODE, CODE_VERIFIER, CLIENT_ID, DEVICE_ID, REDIRECT_URI, STATE) } returns call
            every { call.execute() } returns Result.success(payload)
            And("Doesn't have previous account") {
                every { tokenStorage.accessToken } returns null

                coEvery { tokensHandler.handle(any(), any(), any()) } just runs
                runTest(scheduler) {
                    handler.handle(authResult, onFailCallback)
                }
                Then("Checks whether callbacks are present") {
                    verify { callbacksHolder.isEmpty() }
                }
                Then("state is cleared") {
                    verify { prefsStore.clear() }
                }
                Then("Device id is saved") {
                    verify { deviceIdProvider.setDeviceId(DEVICE_ID) }
                }
                Then("Callbacks are requested from holder") {
                    verify { callbacksHolder.getAll() }
                }
                Then("Auth code is emitted") {
                    verify { callback.onAuthCode(AUTH_CODE_DATA, false) }
                }
                Then("User info fetcher is called") {
                    coVerify { tokensHandler.handle(payload, any(), any()) }
                }
                Then("OnFail callback is not called") {
                    verify(exactly = 0) { onFailCallback() }
                }
            }
        }
        When("Handle is called and api returns success") {
            val authResult = authResultSuccess
            val callback = mockk<VKIDAuthCallback>()
            val call = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            val payload = VKIDTokenPayload(ACCESS_TOKEN_VALUE, REFRESH_TOKEN, ID_TOKEN, 0, USER_ID, STATE, SCOPE)
            every { callbacksHolder.isEmpty() } returns false
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onAuthCode(any(), any()) } just runs
            every { callback.onAuth(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            every { prefsStore.state } returns STATE
            every { prefsStore.clear() } just runs
            every { prefsStore.codeVerifier } returns CODE_VERIFIER
            every { serviceCredentials.clientID } returns CLIENT_ID
            every { serviceCredentials.clientSecret } returns CLIENT_SECRET
            every { serviceCredentials.redirectUri } returns REDIRECT_URI
            every { api.getToken(CODE, CODE_VERIFIER, CLIENT_ID, DEVICE_ID, REDIRECT_URI, STATE) } returns call
            every { call.execute() } returns Result.success(payload)
            And("Has previous account and logout succeds") {
                every { tokenStorage.accessToken } returns ACCESS_TOKEN

                val callbackSlot = slot<VKIDLogoutCallback>()
                val paramsSlot = slot<VKIDLogoutParams>()
                coEvery { loggerOut.logout(capture(callbackSlot), any(), any(), capture(paramsSlot)) } just runs
                val onSuccessSlot = slot<suspend (AccessToken) -> Unit>()
                coEvery { tokensHandler.handle(any(), capture(onSuccessSlot), any()) } just runs
                runTest(scheduler) {
                    handler.handle(authResult, onFailCallback)
                }
                Then("Checks whether callbacks are present") {
                    verify { callbacksHolder.isEmpty() }
                }
                Then("state is cleared") {
                    verify { prefsStore.clear() }
                }
                Then("Device id is saved") {
                    verify { deviceIdProvider.setDeviceId(DEVICE_ID) }
                }

                runTest(scheduler) {
                    onSuccessSlot.captured(ACCESS_TOKEN)
                }
                Then("Logger out is called") {
                    coVerify { loggerOut.logout(capture(callbackSlot), ACCESS_TOKEN_VALUE, false, capture(paramsSlot)) }
                }
                scheduler.advanceUntilIdle()
                callbackSlot.captured.onSuccess()
                Then("Callbacks are requested from holder") {
                    verify { callbacksHolder.getAll() }
                }
                Then("Auth code is emitted") {
                    verify { callback.onAuthCode(AUTH_CODE_DATA, false) }
                }
                Then("OnFail callback is not called") {
                    verify(exactly = 0) { onFailCallback() }
                }
            }
        }

        When("Handle is called and api returns success") {
            val authResult = authResultSuccess
            val callback = mockk<VKIDAuthCallback>()
            val call = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            val payload = VKIDTokenPayload(ACCESS_TOKEN_VALUE, REFRESH_TOKEN, ID_TOKEN, 0, USER_ID, STATE, SCOPE)
            every { callbacksHolder.isEmpty() } returns false
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onAuthCode(any(), any()) } just runs
            every { callback.onAuth(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            every { prefsStore.state } returns STATE
            every { prefsStore.clear() } just runs
            every { prefsStore.codeVerifier } returns CODE_VERIFIER
            every { serviceCredentials.clientID } returns CLIENT_ID
            every { serviceCredentials.clientSecret } returns CLIENT_SECRET
            every { serviceCredentials.redirectUri } returns REDIRECT_URI
            every { api.getToken(CODE, CODE_VERIFIER, CLIENT_ID, DEVICE_ID, REDIRECT_URI, STATE) } returns call
            every { call.execute() } returns Result.success(payload)
            And("Has previous account and logout fails") {
                every { tokenStorage.accessToken } returns ACCESS_TOKEN

                val callbackSlot = slot<VKIDLogoutCallback>()
                val paramsSlot = slot<VKIDLogoutParams>()
                coEvery { loggerOut.logout(capture(callbackSlot), any(), any(), capture(paramsSlot)) } just runs
                val onSuccessSlot = slot<suspend (AccessToken) -> Unit>()
                coEvery { tokensHandler.handle(any(), capture(onSuccessSlot), any()) } just runs
                runTest(scheduler) {
                    handler.handle(authResult, onFailCallback)
                }
                Then("Checks whether callbacks are present") {
                    verify { callbacksHolder.isEmpty() }
                }
                Then("state is cleared") {
                    verify { prefsStore.clear() }
                }
                Then("Device id is saved") {
                    verify { deviceIdProvider.setDeviceId(DEVICE_ID) }
                }

                runTest(scheduler) {
                    onSuccessSlot.captured(ACCESS_TOKEN)
                }
                Then("Logger out is called") {
                    coVerify { loggerOut.logout(capture(callbackSlot), ACCESS_TOKEN_VALUE, false, capture(paramsSlot)) }
                }
                scheduler.advanceUntilIdle()
                callbackSlot.captured.onFail(VKIDLogoutFail.NotAuthenticated(""))
                Then("Callbacks are requested from holder") {
                    verify { callbacksHolder.getAll() }
                }
                Then("Auth code is emitted") {
                    verify { callback.onAuthCode(AUTH_CODE_DATA, false) }
                }
                Then("OnFail callback is not called") {
                    verify(exactly = 0) { onFailCallback() }
                }
            }
        }
    }
})
