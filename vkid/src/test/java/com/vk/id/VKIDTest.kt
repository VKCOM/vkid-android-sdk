package com.vk.id

import com.vk.AuthCallbacksHolder
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.ipc.VkSilentAuthInfoProvider
import com.vk.id.internal.user.UserDataFetcher
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher

internal class VKIDTest : BehaviorSpec({

    val authProvidersChooser = mockk<AuthProvidersChooser>()
    val authProvider = mockk<VKIDAuthProvider>()
    val authOptionsCreator = mockk<AuthOptionsCreator>()
    val authCallbacksHolder = mockk<AuthCallbacksHolder>()
    val testDispatcher = StandardTestDispatcher()
    val userDataFetcher = mockk<UserDataFetcher>()
    val dispatchers = object : CoroutinesDispatchers {
        override val io: CoroutineDispatcher = testDispatcher
    }
    val vkid = VKID(
        object : VKIDDeps {
            override val authProvidersChooser: Lazy<AuthProvidersChooser> = lazy { authProvidersChooser }
            override val authOptionsCreator: AuthOptionsCreator = authOptionsCreator
            override val authCallbacksHolder: AuthCallbacksHolder = authCallbacksHolder
            override val authResultHandler: Lazy<AuthResultHandler> = mockk()
            override val dispatchers: CoroutinesDispatchers = dispatchers
            override val vkSilentAuthInfoProvider: Lazy<VkSilentAuthInfoProvider> = mockk()
            override val userDataFetcher: Lazy<UserDataFetcher> = lazy { userDataFetcher }
        }
    )

    val authParams = VKIDAuthParams { oAuth = OAuth.VK }
    val authOptions = AuthOptions(
        appId = "appId",
        clientSecret = "client secret",
        codeChallenge = "code challenge",
        codeChallengeMethod = "code challenge method",
        deviceId = "device id",
        redirectUri = "redirect uri",
        state = "state",
        locale = "locale",
        theme = "theme",
        webAuthPhoneScreen = false,
        oAuth = null
    )
    val expireTime = System.currentTimeMillis() + 1000

    Given("Auth with VK is called") {
        coEvery { authProvidersChooser.chooseBest(authParams) } returns authProvider
        every { authOptionsCreator.create(authParams) } returns authOptions
        every { authProvider.auth(authOptions) } just runs
        every { authCallbacksHolder.add(any()) } just runs
        launch(testDispatcher) { vkid.authorize(authCallback = mockk(), authParams = authParams) }
        testDispatcher.scheduler.advanceUntilIdle()

        When("Auth result is delivered") {
            AuthEventBridge.listener?.onAuthResult(
                AuthResult.Success(
                    token = "TOKEN",
                    uuid = "uuid",
                    expireTime = expireTime,
                    userId = 123L,
                    firstName = "first name",
                    lastName = "last name",
                    avatar = null,
                    phone = null,
                    oauth = null,
                )
            )
            testDispatcher.scheduler.advanceUntilIdle()

            Then("Auth callback is added") {
                verify { authCallbacksHolder.add(any()) }
            }

            Then("Auth provider is selected") {
                coVerify { authProvider.auth(authOptions) }
            }
        }
    }

    Given("vkid") {
        When("Fetch user data is called") {
            val user = VKIDUser(
                firstName = "first name",
                lastName = "last name",
                phone = "phone",
                photo50 = "photo 50",
                photo100 = "photo 100",
                photo200 = "photo 200",
            )
            coEvery { userDataFetcher.fetchUserData() } returns user
            val result = vkid.fetchUserData()
            Then("User data fetcher is accessed") {
                coVerify { userDataFetcher.fetchUserData() }
            }
            Then("User is returned") {
                result shouldBe Result.success(user)
            }
        }
    }
})
