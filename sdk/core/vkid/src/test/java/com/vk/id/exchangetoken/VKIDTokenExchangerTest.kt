package com.vk.id.exchangetoken

import android.content.Context
import com.vk.id.AccessToken
import com.vk.id.TokensHandler
import com.vk.id.VKIDUser
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.api.VKIDCall
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
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
private const val FIRST_NAME = "first"
private const val LAST_NAME = "last"
private const val PHONE = "phone"
private const val AVATAR = "avatar"
private const val EMAIL = "email"
private const val V1_TOKEN = "V1_TOKEN"
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
)
private val TOKEN_PAYLOAD = VKIDTokenPayload(
    accessToken = ACCESS_TOKEN_VALUE,
    refreshToken = REFRESH_TOKEN,
    idToken = ID_TOKEN,
    expiresIn = -1,
    userId = USER_ID,
)

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class VKIDTokenExchangerTest : BehaviorSpec({

    coroutineTestScope = true

    Given("User info fetcher") {
        val context = mockk<Context>()
        val api = mockk<VKIDApiService>()
        val deviceIdProvider = mockk<DeviceIdProvider>()
        every { deviceIdProvider.getDeviceId(context) } returns DEVICE_ID
        val serviceCredentials = ServiceCredentials(
            clientID = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            redirectUri = REDIRECT_URI,
        )
        val stateGenerator = mockk<StateGenerator>()
        val tokensHandler = mockk<TokensHandler>()
        val dispatchers = mockk<CoroutinesDispatchers>()
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        every { dispatchers.io } returns testDispatcher
        val exchanger = VKIDTokenExchanger(
            context = context,
            api = api,
            deviceIdProvider = deviceIdProvider,
            serviceCredentials = serviceCredentials,
            stateGenerator = stateGenerator,
            tokensHandler = tokensHandler,
            dispatchers = dispatchers,
        )
        every { stateGenerator.regenerateState() } returns STATE
        When("Api returns an error") {
            val call = mockk<VKIDCall<VKIDTokenPayload>>()
            val exception = Exception("message")
            every { call.execute() } returns Result.failure(exception)
            coEvery {
                api.exchangeToken(
                    v1Token = V1_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = STATE,
                )
            } returns call
            val callback = mockk<VKIDExchangeTokenToV2Callback>()
            val fail = VKIDExchangeTokenFail.FailedApiCall("Failed code to refresh token due to: message", exception)
            every { callback.onFail(fail) } just runs
            runTest(scheduler) {
                exchanger.exchange(
                    v1Token = V1_TOKEN,
                    callback = callback,
                )
            }
            Then("Calls callback's onFail") {
                verify { callback.onFail(fail) }
            }
        }
        When("Api returns token") {
            val call = mockk<VKIDCall<VKIDTokenPayload>>()
            every { call.execute() } returns Result.success(TOKEN_PAYLOAD)
            coEvery {
                api.exchangeToken(
                    v1Token = V1_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = STATE,
                )
            } returns call
            val onFailedApiCall = slot<(Throwable) -> Unit>()
            val onFailedOAuthState = slot<() -> Unit>()
            val onSuccess = slot<(AccessToken) -> Unit>()
            val callback = mockk<VKIDExchangeTokenToV2Callback>()
            every { callback.onSuccess(ACCESS_TOKEN) } just runs
            val failedApiCallException = Exception("message")
            val failedApiCallFail = VKIDExchangeTokenFail.FailedApiCall("Failed to fetch user data due to message", failedApiCallException)
            every { callback.onFail(failedApiCallFail) } just runs
            val failedOAuthStateFail = VKIDExchangeTokenFail.FailedOAuthState("Wrong state for getting user info")
            every { callback.onFail(failedOAuthStateFail) } just runs
            coEvery {
                tokensHandler.handle(
                    TOKEN_PAYLOAD,
                    capture(onSuccess),
                    capture(onFailedApiCall),
                    capture(onFailedOAuthState),
                )
            } just runs
            runTest(scheduler) {
                exchanger.exchange(
                    v1Token = V1_TOKEN,
                    callback = callback,
                )
            }
            Then("Calls onFailedOAuthState") {
                onSuccess.captured(ACCESS_TOKEN)
                verify { callback.onSuccess(ACCESS_TOKEN) }
                onFailedApiCall.captured(failedApiCallException)
                verify { callback.onFail(failedApiCallFail) }
                onFailedOAuthState.captured()
                verify { callback.onFail(failedOAuthStateFail) }
            }
        }
    }
})
