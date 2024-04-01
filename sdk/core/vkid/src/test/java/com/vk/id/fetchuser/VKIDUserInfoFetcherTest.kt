package com.vk.id.fetchuser

import com.vk.id.VKIDUser
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.api.VKIDCall
import com.vk.id.internal.api.dto.VKIDUserInfoPayload
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.state.StateGenerator
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.mockk.called
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
private const val ACCESS_TOKEN = "access token"
private const val DEVICE_ID = "device id"
private const val STATE = "state"
private const val FIRST_NAME = "first"
private const val LAST_NAME = "last"
private const val PHONE = "phone"
private const val AVATAR = "avatar"
private const val EMAIL = "email"
private val USER_INFO_PAYLOAD = VKIDUserInfoPayload(
    firstName = FIRST_NAME,
    lastName = LAST_NAME,
    phone = PHONE,
    avatar = AVATAR,
    email = EMAIL,
)
private val VKID_USER = VKIDUser(
    firstName = FIRST_NAME,
    lastName = LAST_NAME,
    phone = PHONE,
    photo50 = null,
    photo100 = null,
    photo200 = AVATAR,
    email = EMAIL,
)

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class VKIDUserInfoFetcherTest : BehaviorSpec({

    coroutineTestScope = true

    Given("User info fetcher") {
        val deviceIdProvider = mockk<DeviceIdProvider>()
        every { deviceIdProvider.getDeviceId() } returns DEVICE_ID
        val api = mockk<VKIDApiService>()
        val stateGenerator = mockk<StateGenerator>()
        val serviceCredentials = ServiceCredentials(
            clientID = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            redirectUri = REDIRECT_URI,
        )
        val dispatchers = mockk<CoroutinesDispatchers>()
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        every { dispatchers.io } returns testDispatcher
        val fetcher = VKIDUserInfoFetcher(
            api = api,
            stateGenerator = stateGenerator,
            serviceCredentials = serviceCredentials,
            dispatchers = dispatchers,
            deviceIdProvider = deviceIdProvider,
        )
        every { stateGenerator.regenerateState() } returns STATE
        When("Api returns an error") {
            val call = mockk<VKIDCall<VKIDUserInfoPayload>>()
            val exception = Exception()
            every { call.execute() } returns Result.failure(exception)
            coEvery {
                api.getUserInfo(
                    accessToken = ACCESS_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = STATE
                )
            } returns call
            val onSuccess = mockk<(VKIDUser) -> Unit>()
            val onFailedApiCall = mockk<(Throwable) -> Unit>()
            every { onFailedApiCall(exception) } just runs
            runTest(scheduler) {
                fetcher.fetch(
                    accessToken = ACCESS_TOKEN,
                    onSuccess = onSuccess,
                    onFailedApiCall = onFailedApiCall,
                )
            }
            Then("Calls onFailedApiCall") {
                verify { onSuccess wasNot called }
                verify { onFailedApiCall(exception) }
            }
        }
        When("Api returns user") {
            val call = mockk<VKIDCall<VKIDUserInfoPayload>>()
            every { call.execute() } returns Result.success(USER_INFO_PAYLOAD)
            coEvery {
                api.getUserInfo(
                    accessToken = ACCESS_TOKEN,
                    clientId = CLIENT_ID,
                    deviceId = DEVICE_ID,
                    state = STATE
                )
            } returns call
            val onFailedApiCall = mockk<(Throwable) -> Unit>()
            val onSuccess = mockk<(VKIDUser) -> Unit>()
            every { onSuccess(VKID_USER) } just runs
            runTest(scheduler) {
                fetcher.fetch(
                    accessToken = ACCESS_TOKEN,
                    onSuccess = onSuccess,
                    onFailedApiCall = onFailedApiCall,
                )
            }
            Then("Calls onFailedOAuthState") {
                verify { onSuccess(VKID_USER) }
                verify { onFailedApiCall wasNot called }
            }
        }
    }
})
