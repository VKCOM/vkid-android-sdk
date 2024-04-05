package com.vk.id

import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.api.VKIDCall
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.store.PrefsStore
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecGivenContainerScope
import io.kotest.core.test.testCoroutineScheduler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

private const val ERROR_MESSAGE = "Error message"
private val error = IllegalStateException("Error")
private const val ACCESS_TOKEN = "access token"
private const val REFRESH_TOKEN = "refresh token"
private const val ID_TOKEN = "id token"
private const val USER_ID = 0L
private const val EXPIRE_TIME = 0L
private const val STATE = "state"
private const val DIFFERENT_STATE = "differentstate"
private const val CODE_VERIFIER = "code verifier"
private const val CODE = "code"
private const val DEVICE_ID = "device id"
private const val CLIENT_ID = "client id"
private const val CLIENT_SECRET = "client secret"
private const val REDIRECT_URI = "redirect uri"
private val authResultSuccess = AuthResult.Success(
    expireTime = EXPIRE_TIME,
    oauth = AuthResult.OAuth(CODE, STATE, CODE_VERIFIER),
    deviceId = DEVICE_ID
)

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class AuthResultHandlerTest : BehaviorSpec({

    coroutineTestScope = true

    Given("An AuthResultHandler") {
        val callbacksHolder = mockk<AuthCallbacksHolder>()
        val deviceIdProvider = mockk<DeviceIdProvider>()
        val prefsStore = mockk<PrefsStore>()
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        val dispatchers = mockk<CoroutinesDispatchers>()
        every { dispatchers.io } returns testDispatcher
        val api = mockk<VKIDApiService>()
        val serviceCredentials = mockk<ServiceCredentials>()
        val tokensHandler = mockk<TokensHandler>()
        val handler = AuthResultHandler(
            dispatchers = dispatchers,
            callbacksHolder = callbacksHolder,
            deviceIdProvider = deviceIdProvider,
            prefsStore = prefsStore,
            serviceCredentials = serviceCredentials,
            api = api,
            tokensHandler = tokensHandler
        )

        suspend fun BehaviorSpecGivenContainerScope.whenHandleIsCalledWithFail(
            authResult: AuthResult,
            authFail: VKIDAuthFail
        ) = When("Handle is called with ${authResult::class.simpleName}") {
            val callback = mockk<VKIDAuthCallback>()
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onFail(any()) } just runs
            every { callbacksHolder.clear() } just runs
            handler.handle(authResult)
            Then("Callbacks are requested from holder") {
                verify { callbacksHolder.getAll() }
            }
            Then("It is emitted") {
                verify { callback.onFail(authFail) }
            }
            Then("Callbacks holder is cleared") {
                verify { callbacksHolder.clear() }
            }
        }
        whenHandleIsCalledWithFail(
            AuthResult.Canceled(ERROR_MESSAGE),
            VKIDAuthFail.Canceled(ERROR_MESSAGE)
        )
        whenHandleIsCalledWithFail(
            AuthResult.NoBrowserAvailable(ERROR_MESSAGE, error),
            VKIDAuthFail.NoBrowserAvailable(ERROR_MESSAGE, error)
        )
        whenHandleIsCalledWithFail(
            AuthResult.AuthActiviyResultFailed(ERROR_MESSAGE, error),
            VKIDAuthFail.FailedRedirectActivity(ERROR_MESSAGE, error)
        )

        When("Auth result doesn't have oAuth") {
            val authResult = authResultSuccess.copy(oauth = null)
            val callback = mockk<VKIDAuthCallback>()
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onFail(any()) } just runs
            every { callbacksHolder.clear() } just runs
            handler.handle(authResult)
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
        }

        When("Handle is called with wrong state") {
            val authResult = authResultSuccess
            val callback = mockk<VKIDAuthCallback>()
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onFail(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            every { prefsStore.state } returns DIFFERENT_STATE
            every { prefsStore.clear() } just runs
            every { prefsStore.codeVerifier } returns CODE_VERIFIER
            runTest(scheduler) { handler.handle(authResult) }
            scheduler.advanceUntilIdle()
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
        }
        When("Handle is called and api returns an error") {
            val authResult = authResultSuccess
            val callback = mockk<VKIDAuthCallback>()
            val call = mockk<VKIDCall<VKIDTokenPayload>>()
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
            runTest(scheduler) { handler.handle(authResult) }
            scheduler.advanceUntilIdle()
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
                verify { callback.onAuthCode(AuthCodeData(CODE), false) }
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
        }
        When("Handle is called and api returns success") {
            val authResult = authResultSuccess
            val callback = mockk<VKIDAuthCallback>()
            val call = mockk<VKIDCall<VKIDTokenPayload>>()
            val payload = VKIDTokenPayload(ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, 0, USER_ID, STATE)
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onAuthCode(any(), any()) } just runs
            every { callback.onSuccess(any()) } just runs
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
            coEvery { tokensHandler.handle(any(), null, any(), any()) } just runs
            runTest(scheduler) { handler.handle(authResult) }
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
                verify { callback.onAuthCode(AuthCodeData(CODE), false) }
            }
            Then("User info fetcher is called") {
                coVerify { tokensHandler.handle(payload, null, any(), any()) }
            }
        }
    }
})
