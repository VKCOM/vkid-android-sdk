package com.vk.id

import android.content.Context
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
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

private const val ERROR_MESSAGE = "Error message"
private val error = IllegalStateException("Error")
private const val TOKEN = "token"
private const val USER_ID = 0L
private const val EXPIRE_TIME = 0L
private const val UUID = "uuid"
private const val DIFFERENT_UUID = "different uuid"
private const val STATE = "state"
private const val DIFFERENT_STATE = "differentstate"
private const val CODE_VERIFIER = "code verifier"
private const val CODE = "code"
private const val CLIENT_ID = "client id"
private const val CLIENT_SECRET = "client secret"
private const val REDIRECT_URI = "redirect uri"
private val authResultSuccess = AuthResult.Success(
    token = TOKEN,
    uuid = UUID,
    expireTime = EXPIRE_TIME,
    userId = USER_ID,
    lastName = "last name",
    firstName = "first name",
    avatar = "avatar",
    phone = "phone",
    oauth = AuthResult.OAuth(CODE, STATE, CODE_VERIFIER),
)

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class AuthResultHandlerTest : BehaviorSpec({

    coroutineTestScope = true

    Given("An AuthResultHandler") {
        val callbacksHolder = mockk<AuthCallbacksHolder>()
        val deviceIdProvider = mockk<DeviceIdProvider>()
        val prefsStore = mockk<PrefsStore>()
        val context = mockk<Context>()
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        val dispatchers = mockk<CoroutinesDispatchers>()
        every { dispatchers.io } returns testDispatcher
        val api = mockk<VKIDApiService>()
        val serviceCredentials = mockk<ServiceCredentials>()
        val handler = AuthResultHandler(
            appContext = context,
            dispatchers = dispatchers,
            callbacksHolder = callbacksHolder,
            deviceIdProvider = deviceIdProvider,
            prefsStore = prefsStore,
            serviceCredentials = serviceCredentials,
            api = api
        )

        suspend fun BehaviorSpecGivenContainerScope.whenHandleIsCalledWithFail(
            authResult: AuthResult,
            authFail: VKIDAuthFail
        ) = When("Handle is called with ${authResult::class.simpleName}") {
            val callback = mockk<VKID.AuthCallback>()
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
            val callback = mockk<VKID.AuthCallback>()
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

        When("Handle is called with wrong uuid") {
            val authResult = authResultSuccess
            val callback = mockk<VKID.AuthCallback>()
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onFail(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { deviceIdProvider.getDeviceId(context) } returns DIFFERENT_UUID
            every { prefsStore.state } returns STATE
            every { prefsStore.codeVerifier } returns CODE_VERIFIER
            TestScope(scheduler).launch { handler.handle(authResult) }
            scheduler.advanceUntilIdle()
            Then("Callbacks are requested from holder") {
                verify { callbacksHolder.getAll() }
            }
            Then("It is emitted") {
                verify {
                    callback.onFail(
                        match {
                            it is VKIDAuthFail.FailedOAuthState &&
                                it.description == "Invalid uuid"
                        }
                    )
                }
            }
            Then("Callbacks holder is cleared") {
                verify { callbacksHolder.clear() }
            }
        }
        When("Handle is called with wrong state") {
            val authResult = authResultSuccess
            val callback = mockk<VKID.AuthCallback>()
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onFail(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { deviceIdProvider.getDeviceId(context) } returns UUID
            every { prefsStore.state } returns DIFFERENT_STATE
            every { prefsStore.codeVerifier } returns CODE_VERIFIER
            TestScope(scheduler).launch { handler.handle(authResult) }
            scheduler.advanceUntilIdle()
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
            val callback = mockk<VKID.AuthCallback>()
            val call = mockk<VKIDCall<VKIDTokenPayload>>()
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onFail(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { deviceIdProvider.getDeviceId(context) } returns UUID
            every { prefsStore.state } returns STATE
            every { prefsStore.codeVerifier } returns CODE_VERIFIER
            every { serviceCredentials.clientID } returns CLIENT_ID
            every { serviceCredentials.clientSecret } returns CLIENT_SECRET
            every { serviceCredentials.redirectUri } returns REDIRECT_URI
            every { api.getToken(CODE, CODE_VERIFIER, CLIENT_ID, CLIENT_SECRET, UUID, REDIRECT_URI) } returns call
            every { call.execute() } returns Result.failure(error)
            TestScope(scheduler).launch { handler.handle(authResult) }
            scheduler.advanceUntilIdle()
            Then("Callbacks are requested from holder") {
                verify { callbacksHolder.getAll() }
            }
            Then("It is emitted") {
                verify { callback.onFail(VKIDAuthFail.FailedApiCall("Failed code to token exchange api call", error)) }
            }
            Then("Callbacks holder is cleared") {
                verify { callbacksHolder.clear() }
            }
        }
        When("Handle is called and api returns success") {
            val authResult = authResultSuccess
            val callback = mockk<VKID.AuthCallback>()
            val call = mockk<VKIDCall<VKIDTokenPayload>>()
            val payload = VKIDTokenPayload(TOKEN, 0, USER_ID, "", "", "")
            every { callbacksHolder.getAll() } returns setOf(callback)
            every { callback.onSuccess(any()) } just runs
            every { callbacksHolder.clear() } just runs
            every { deviceIdProvider.getDeviceId(context) } returns UUID
            every { prefsStore.state } returns STATE
            every { prefsStore.codeVerifier } returns CODE_VERIFIER
            every { serviceCredentials.clientID } returns CLIENT_ID
            every { serviceCredentials.clientSecret } returns CLIENT_SECRET
            every { serviceCredentials.redirectUri } returns REDIRECT_URI
            every { api.getToken(CODE, CODE_VERIFIER, CLIENT_ID, CLIENT_SECRET, UUID, REDIRECT_URI) } returns call
            every { call.execute() } returns Result.success(payload)
            TestScope(scheduler).launch(testDispatcher) { handler.handle(authResult) }
            scheduler.advanceUntilIdle()
            Then("Callbacks are requested from holder") {
                verify { callbacksHolder.getAll() }
            }
            Then("It is emitted") {
                verify {
                    callback.onSuccess(
                        AccessToken(
                            TOKEN,
                            USER_ID,
                            -1,
                            VKIDUser(
                                firstName = "first name",
                                lastName = "last name",
                                phone = "",
                                photo200 = "avatar",
                                email = ""
                            )
                        )
                    )
                }
            }
            Then("Callbacks holder is cleared") {
                verify { callbacksHolder.clear() }
            }
        }
    }
})
