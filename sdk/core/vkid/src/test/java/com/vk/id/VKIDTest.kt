@file:OptIn(InternalVKIDApi::class)

package com.vk.id

import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApi
import com.vk.id.internal.auth.AuthCallbacksHolder
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
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class VKIDTest : BehaviorSpec({

    coroutineTestScope = true

    Given("Auth with VK is called") {
        val authProvidersChooser = mockk<AuthProvidersChooser>()
        val authProvider = mockk<VKIDAuthProvider>()
        val authOptionsCreator = mockk<AuthOptionsCreator>()
        val authCallbacksHolder = mockk<AuthCallbacksHolder>()
        val authResultHandler = mockk<AuthResultHandler>()
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        val userDataFetcher = mockk<UserDataFetcher>()
        val dispatchers = mockk<CoroutinesDispatchers>()
        every { dispatchers.io } returns testDispatcher
        val vkid = VKID(
            object : VKIDDeps {
                override val authProvidersChooser: Lazy<AuthProvidersChooser> = lazy { authProvidersChooser }
                override val authOptionsCreator: AuthOptionsCreator = authOptionsCreator
                override val authCallbacksHolder: AuthCallbacksHolder = authCallbacksHolder
                override val authResultHandler: Lazy<AuthResultHandler> = lazy { authResultHandler }
                override val dispatchers: CoroutinesDispatchers = dispatchers
                override val vkSilentAuthInfoProvider: Lazy<VkSilentAuthInfoProvider> = mockk()
                override val userDataFetcher: Lazy<UserDataFetcher> = lazy { userDataFetcher }
                override val api: Lazy<VKIDApi> = lazy { mockk() }
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
        coEvery { authProvidersChooser.chooseBest(authParams) } returns authProvider
        every { authOptionsCreator.create(authParams) } returns authOptions
        every { authProvider.auth(authOptions) } just runs
        every { authCallbacksHolder.add(any()) } just runs
        coEvery { authResultHandler.handle(any()) } just runs
        TestScope(scheduler).launch {
            vkid.authorize(authCallback = mockk(), authParams = authParams)
        }
        scheduler.advanceUntilIdle()

        When("Auth result is delivered") {
            TestScope(scheduler).launch {
                AuthEventBridge.listener?.onAuthResult(
                    AuthResult.Success(
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
            }
            scheduler.advanceUntilIdle()

            Then("Auth result is handled") {
                coVerify { authResultHandler.handle(any()) }
            }

            Then("Auth callback is added") {
                verify { authCallbacksHolder.add(any()) }
            }

            Then("Auth provider is selected") {
                coVerify { authProvider.auth(authOptions) }
            }
        }
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
