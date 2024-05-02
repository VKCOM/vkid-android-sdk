@file:OptIn(InternalVKIDApi::class)

package com.vk.id

import com.vk.id.analytics.stat.StatTracker
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.exchangetoken.VKIDTokenExchanger
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.ipc.VkSilentAuthInfoProvider
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.logout.VKIDLoggerOut
import com.vk.id.network.InternalVKIDApiContract
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.refreshuser.VKIDUserRefresher
import com.vk.id.storage.InternalVKIDEncryptedSharedPreferencesStorage
import com.vk.id.storage.TokenStorage
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
<<<<<<< HEAD
        val dispatchers = mockk<VKIDCoroutinesDispatchers>()
=======
        val dispatchers = mockk<CoroutinesDispatchers>()
        val statTracker = mockk<StatTracker>(relaxed = true)
>>>>>>> develop
        every { dispatchers.io } returns testDispatcher
        val vkid = VKID(
            object : VKIDDeps {
                override val authProvidersChooser: Lazy<AuthProvidersChooser> = lazy { authProvidersChooser }
                override val authOptionsCreator: AuthOptionsCreator = authOptionsCreator
                override val authCallbacksHolder: AuthCallbacksHolder = authCallbacksHolder
                override val authResultHandler: Lazy<AuthResultHandler> = lazy { authResultHandler }
<<<<<<< HEAD
                override val dispatchers: VKIDCoroutinesDispatchers = dispatchers
=======
                override val dispatchers: CoroutinesDispatchers = dispatchers
                override val statTracker: StatTracker = statTracker
>>>>>>> develop
                override val vkSilentAuthInfoProvider: Lazy<VkSilentAuthInfoProvider> = mockk()
                override val userDataFetcher: Lazy<UserDataFetcher> = lazy { userDataFetcher }
                override val api: Lazy<InternalVKIDApiContract> = lazy { mockk() }
                override val tokenRefresher: Lazy<VKIDTokenRefresher> = lazy { mockk() }
                override val tokenExchanger: Lazy<VKIDTokenExchanger> = lazy { mockk() }
                override val userRefresher: Lazy<VKIDUserRefresher> = lazy { mockk() }
                override val loggerOut: Lazy<VKIDLoggerOut> = lazy { mockk() }
                override val tokenStorage: TokenStorage = mockk()
                override val deviceIdStorage: Lazy<InternalVKIDDeviceIdProvider.DeviceIdStorage> = lazy { mockk() }
                override val prefsStore: Lazy<InternalVKIDPrefsStore> = lazy { mockk() }
                override val encryptedSharedPreferencesStorage: Lazy<InternalVKIDEncryptedSharedPreferencesStorage> =
                    lazy { mockk() }
            }
        )

        When("VKID initialized") {
            Then("Analytics vkid_init event is send") {
                verify {
                    statTracker.trackEvent(
                        "sdk_init",
                        match { it.name == "sdk_type" && it.strValue == "vkid" }
                    )
                }
            }
        }

        val authParams = VKIDAuthParams { oAuth = OAuth.VK }
        val authOptions = AuthOptions(
            appId = "appId",
            clientSecret = "client secret",
            codeChallenge = "code challenge",
            codeChallengeMethod = "code challenge method",
            redirectUri = "redirect uri",
            state = "state",
            locale = "locale",
            theme = "theme",
            webAuthPhoneScreen = false,
            oAuth = null,
<<<<<<< HEAD
            prompt = "",
            scopes = emptySet(),
=======
            extraParams = null
>>>>>>> develop
        )
        coEvery { authProvidersChooser.chooseBest(authParams) } returns authProvider
        every { authOptionsCreator.create(authParams) } returns authOptions
        every { authProvider.auth(authOptions) } just runs
        every { authCallbacksHolder.add(any()) } just runs
        coEvery { authResultHandler.handle(any()) } just runs
        TestScope(scheduler).launch {
            vkid.authorize(callback = mockk(), params = authParams)
        }
        scheduler.advanceUntilIdle()

        When("Auth result is delivered") {
            TestScope(scheduler).launch {
                AuthEventBridge.listener?.onAuthResult(
                    AuthResult.Success(
                        oauth = null,
                        "device id"
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
