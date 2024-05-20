@file:OptIn(InternalVKIDApi::class)

package com.vk.id.exchangetoken

import com.vk.id.AccessToken
import com.vk.id.TokensHandler
import com.vk.id.VKIDUser
import com.vk.id.auth.AuthCodeData
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.VKIDCodePayload
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.network.InternalVKIDCall
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
private const val REFRESH_TOKEN = "refresh token"
private const val ID_TOKEN = "id token"
private const val DEVICE_ID = "device id"
private const val STATE = "state"
private const val CODE_EXTERNAL_STATE = "code exchange state"
private const val CODE_STATE = "code state"
private const val FIRST_NAME = "first"
private const val LAST_NAME = "last"
private const val PHONE = "phone"
private const val AVATAR = "avatar"
private const val EMAIL = "email"
private const val V1_TOKEN = "V1_TOKEN"
private const val USER_ID = 100L
private const val CODE_CHALLENGE = "code challenge"
private const val CODE_VERIFIER = "code verifier"
private const val CODE = "authorization code"
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
)
private val CODE_PAYLOAD = VKIDCodePayload(
    code = CODE,
    state = CODE_STATE,
    deviceId = DEVICE_ID,
)
private val TOKEN_PAYLOAD = VKIDTokenPayload(
    accessToken = ACCESS_TOKEN_VALUE,
    refreshToken = REFRESH_TOKEN,
    idToken = ID_TOKEN,
    expiresIn = -1,
    userId = USER_ID,
    state = STATE,
)

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class VKIDTokenExchangerTest : BehaviorSpec({

    coroutineTestScope = true
    isolationMode = IsolationMode.InstancePerLeaf

    Given("User info fetcher") {
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
        val pkceGenerator = mockk<PkceGeneratorSHA256>()
        val exchanger = VKIDTokenExchanger(
            api = api,
            deviceIdProvider = deviceIdProvider,
            serviceCredentials = serviceCredentials,
            stateGenerator = stateGenerator,
            tokensHandler = tokensHandler,
            dispatchers = dispatchers,
            prefsStore = prefsStore,
            pkceGenerator = pkceGenerator,
        )
        every { stateGenerator.regenerateState() } returns CODE_STATE andThen STATE
        When("Api returns an error") {
            every { pkceGenerator.generateRandomCodeVerifier(any()) } returns CODE_VERIFIER
            every { pkceGenerator.deriveCodeVerifierChallenge(CODE_VERIFIER) } returns CODE_CHALLENGE
            every { prefsStore.clear() } just runs
            val call = mockk<InternalVKIDCall<VKIDCodePayload>>()
            val exception = Exception("message")
            every { call.execute() } returns Result.failure(exception)
            coEvery {
                api.exchangeToken(
                    v1Token = V1_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = CODE_STATE,
                    codeChallenge = CODE_CHALLENGE,
                )
            } returns call
            val callback = mockk<VKIDExchangeTokenCallback>()
            val fail = VKIDExchangeTokenFail.FailedApiCall("Failed to exchange token due to: message", exception)
            every { callback.onFail(fail) } just runs
            runTest(scheduler) {
                exchanger.exchange(
                    v1Token = V1_TOKEN,
                    callback = callback,
                    params = VKIDExchangeTokenParams {}
                )
            }
            Then("Clears prefs store") {
                verify { prefsStore.clear() }
            }
            Then("Calls callback's onFail") {
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns wrong state") {
            every { prefsStore.clear() } just runs
            val call = mockk<InternalVKIDCall<VKIDCodePayload>>()
            every { call.execute() } returns Result.success(CODE_PAYLOAD.copy(state = "wrong state"))
            coEvery {
                api.exchangeToken(
                    v1Token = V1_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = CODE_STATE,
                    codeChallenge = CODE_CHALLENGE,
                )
            } returns call
            val callback = mockk<VKIDExchangeTokenCallback>()
            val fail = VKIDExchangeTokenFail.FailedOAuthState("Invalid state during code receiving")
            every { callback.onFail(fail) } just runs
            runTest(scheduler) {
                exchanger.exchange(
                    v1Token = V1_TOKEN,
                    callback = callback,
                    params = VKIDExchangeTokenParams {
                        codeChallenge = CODE_CHALLENGE
                    }
                )
            }
            Then("Clears prefs store") {
                verify { prefsStore.clear() }
            }
            Then("Calls callback's onFail") {
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns code and code challenge is provided") {
            every { prefsStore.clear() } just runs
            val call = mockk<InternalVKIDCall<VKIDCodePayload>>()
            every { call.execute() } returns Result.success(CODE_PAYLOAD)
            coEvery {
                api.exchangeToken(
                    v1Token = V1_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = CODE_STATE,
                    codeChallenge = CODE_CHALLENGE,
                )
            } returns call
            val callback = mockk<VKIDExchangeTokenCallback>()
            every { callback.onAuthCode(AuthCodeData(CODE), true) } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            runTest(scheduler) {
                exchanger.exchange(
                    v1Token = V1_TOKEN,
                    callback = callback,
                    params = VKIDExchangeTokenParams {
                        codeChallenge = CODE_CHALLENGE
                    }
                )
            }
            Then("Clears prefs store") {
                verify { prefsStore.clear() }
            }
            Then("Saves device id") {
                verify { deviceIdProvider.setDeviceId(DEVICE_ID) }
            }
            Then("Calls callback.onAuthCode") {
                verify { callback.onAuthCode(AuthCodeData(CODE), true) }
            }
        }
        When("Api returns code and token getting fails") {
            every { pkceGenerator.generateRandomCodeVerifier(any()) } returns CODE_VERIFIER
            every { pkceGenerator.deriveCodeVerifierChallenge(CODE_VERIFIER) } returns CODE_CHALLENGE
            every { prefsStore.clear() } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            val call = mockk<InternalVKIDCall<VKIDCodePayload>>()
            every { call.execute() } returns Result.success(CODE_PAYLOAD)
            coEvery {
                api.exchangeToken(
                    v1Token = V1_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = CODE_STATE,
                    codeChallenge = CODE_CHALLENGE,
                )
            } returns call
            val getTokenCall = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            val failedApiCallException = Exception("message")
            every { getTokenCall.execute() } returns Result.failure(failedApiCallException)
            coEvery {
                api.getToken(
                    code = CODE,
                    codeVerifier = CODE_VERIFIER,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = STATE,
                    redirectUri = REDIRECT_URI,
                )
            } returns getTokenCall
            val callback = mockk<VKIDExchangeTokenCallback>()
            val fail = VKIDExchangeTokenFail.FailedApiCall("Failed to exchange code due to: message", failedApiCallException)
            every { callback.onFail(fail) } just runs
            every { callback.onAuthCode(AuthCodeData(CODE), false) } just runs
            runTest(scheduler) {
                exchanger.exchange(
                    v1Token = V1_TOKEN,
                    callback = callback,
                    params = VKIDExchangeTokenParams {}
                )
            }
            Then("Clears prefs store") {
                verify { prefsStore.clear() }
            }
            Then("Saves device id") {
                verify { deviceIdProvider.setDeviceId(DEVICE_ID) }
            }
            Then("Calls callback.onAuthCode") {
                verify { callback.onAuthCode(AuthCodeData(CODE), false) }
            }
            Then("Calls callback's onFail") {
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns code and token getting returns wrong state") {
            every { pkceGenerator.generateRandomCodeVerifier(any()) } returns CODE_VERIFIER
            every { pkceGenerator.deriveCodeVerifierChallenge(CODE_VERIFIER) } returns CODE_CHALLENGE
            every { prefsStore.clear() } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            val call = mockk<InternalVKIDCall<VKIDCodePayload>>()
            every { call.execute() } returns Result.success(CODE_PAYLOAD)
            coEvery {
                api.exchangeToken(
                    v1Token = V1_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = CODE_STATE,
                    codeChallenge = CODE_CHALLENGE,
                )
            } returns call
            val getTokenCall = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            every { getTokenCall.execute() } returns Result.success(TOKEN_PAYLOAD.copy(state = "wrong state"))
            coEvery {
                api.getToken(
                    code = CODE,
                    codeVerifier = CODE_VERIFIER,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = CODE_EXTERNAL_STATE,
                    redirectUri = REDIRECT_URI,
                )
            } returns getTokenCall
            val callback = mockk<VKIDExchangeTokenCallback>()
            every { callback.onAuthCode(AuthCodeData(CODE), false) } just runs
            val fail = VKIDExchangeTokenFail.FailedOAuthState("Invalid state during code exchange")
            every { callback.onFail(fail) } just runs
            runTest(scheduler) {
                exchanger.exchange(
                    v1Token = V1_TOKEN,
                    callback = callback,
                    params = VKIDExchangeTokenParams {
                        codeExchangeState = CODE_EXTERNAL_STATE
                    }
                )
            }
            Then("Clears prefs store") {
                verify { prefsStore.clear() }
            }
            Then("Saves device id") {
                verify { deviceIdProvider.setDeviceId(DEVICE_ID) }
            }
            Then("Calls callback.onAuthCode") {
                verify { callback.onAuthCode(AuthCodeData(CODE), false) }
            }
            Then("Calls callback's onFail") {
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns code and token getting fails") {
            every { pkceGenerator.generateRandomCodeVerifier(any()) } returns CODE_VERIFIER
            every { pkceGenerator.deriveCodeVerifierChallenge(CODE_VERIFIER) } returns CODE_CHALLENGE
            every { prefsStore.clear() } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            val call = mockk<InternalVKIDCall<VKIDCodePayload>>()
            every { call.execute() } returns Result.success(CODE_PAYLOAD)
            coEvery {
                api.exchangeToken(
                    v1Token = V1_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = CODE_STATE,
                    codeChallenge = CODE_CHALLENGE,
                )
            } returns call
            val getTokenCall = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            val failedApiCallException = Exception("message")
            every { getTokenCall.execute() } returns Result.success(TOKEN_PAYLOAD)
            coEvery {
                api.getToken(
                    code = CODE,
                    codeVerifier = CODE_VERIFIER,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = STATE,
                    redirectUri = REDIRECT_URI,
                )
            } returns getTokenCall
            val onFailedApiCall = slot<(Throwable) -> Unit>()
            val onSuccess = slot<suspend (AccessToken) -> Unit>()
            val callback = mockk<VKIDExchangeTokenCallback>()
            every { callback.onAuthCode(AuthCodeData(CODE), false) } just runs
            val fail = VKIDExchangeTokenFail.FailedApiCall("Failed to fetch user data", failedApiCallException)
            every { callback.onFail(fail) } just runs
            coEvery {
                tokensHandler.handle(
                    TOKEN_PAYLOAD,
                    capture(onSuccess),
                    capture(onFailedApiCall),
                )
            } just runs
            runTest(scheduler) {
                exchanger.exchange(
                    v1Token = V1_TOKEN,
                    callback = callback,
                    params = VKIDExchangeTokenParams {}
                )
            }
            Then("Clears prefs store") {
                verify { prefsStore.clear() }
            }
            Then("Saves device id") {
                verify { deviceIdProvider.setDeviceId(DEVICE_ID) }
            }
            Then("Calls callback.onAuthCode") {
                verify { callback.onAuthCode(AuthCodeData(CODE), false) }
            }
            Then("Calls callback's onFail") {
                onFailedApiCall.captured(failedApiCallException)
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns code and token getting succeeds") {
            every { pkceGenerator.generateRandomCodeVerifier(any()) } returns CODE_VERIFIER
            every { pkceGenerator.deriveCodeVerifierChallenge(CODE_VERIFIER) } returns CODE_CHALLENGE
            every { prefsStore.clear() } just runs
            every { deviceIdProvider.setDeviceId(DEVICE_ID) } just runs
            val call = mockk<InternalVKIDCall<VKIDCodePayload>>()
            every { call.execute() } returns Result.success(CODE_PAYLOAD)
            coEvery {
                api.exchangeToken(
                    v1Token = V1_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = CODE_STATE,
                    codeChallenge = CODE_CHALLENGE,
                )
            } returns call
            val getTokenCall = mockk<InternalVKIDCall<VKIDTokenPayload>>()
            every { getTokenCall.execute() } returns Result.success(TOKEN_PAYLOAD)
            coEvery {
                api.getToken(
                    code = CODE,
                    codeVerifier = CODE_VERIFIER,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = STATE,
                    redirectUri = REDIRECT_URI,
                )
            } returns getTokenCall
            val onFailedApiCall = slot<(Throwable) -> Unit>()
            val onSuccess = slot<suspend (AccessToken) -> Unit>()
            val callback = mockk<VKIDExchangeTokenCallback>()
            every { callback.onAuth(ACCESS_TOKEN) } just runs
            every { callback.onAuthCode(AuthCodeData(CODE), false) } just runs
            coEvery {
                tokensHandler.handle(
                    TOKEN_PAYLOAD,
                    capture(onSuccess),
                    capture(onFailedApiCall),
                )
            } just runs
            runTest(scheduler) {
                exchanger.exchange(
                    v1Token = V1_TOKEN,
                    callback = callback,
                    params = VKIDExchangeTokenParams {}
                )
            }
            Then("Clears prefs store") {
                verify { prefsStore.clear() }
            }
            Then("Saves device id") {
                verify { deviceIdProvider.setDeviceId(DEVICE_ID) }
            }
            Then("Calls callback.onAuthCode") {
                verify { callback.onAuthCode(AuthCodeData(CODE), false) }
            }
            Then("Calls callback's onFail") {
                onSuccess.captured(ACCESS_TOKEN)
                verify { callback.onAuth(ACCESS_TOKEN) }
            }
        }
    }
})
