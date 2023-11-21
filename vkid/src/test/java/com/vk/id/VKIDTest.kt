package com.vk.id

import android.content.Context
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.auth.app.SilentAuthServicesProvider
import com.vk.id.internal.auth.app.TrustedProvidersCache
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.ipc.VkSilentAuthInfoProvider
import com.vk.id.internal.store.PrefsStore
import com.vk.id.internal.user.UserDataFetcher
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher

internal class VKIDTest : BehaviorSpec({

    val context = mockk<Context>()
    val authProvidersChooser = mockk<AuthProvidersChooser>()
    val authProvider = mockk<VKIDAuthProvider>()
    val authOptionsCreator = mockk<AuthOptionsCreator>()
    val testDispatcher = StandardTestDispatcher()
    val dispatchers = object : CoroutinesDispatchers {
        override val io: CoroutineDispatcher = testDispatcher
    }
    val vkid = VKID(
        object : VKIDDeps {
            override val api: Lazy<VKIDApiService> = mockk()
            override val appContext: Context = context
            override val authProvidersChooser: Lazy<AuthProvidersChooser> = lazy { authProvidersChooser }
            override val authOptionsCreator: AuthOptionsCreator = authOptionsCreator
            override val deviceIdProvider: Lazy<DeviceIdProvider> = mockk()
            override val dispatchers: CoroutinesDispatchers = dispatchers
            override val prefsStore: Lazy<PrefsStore> = mockk()
            override val pkceGenerator: Lazy<PkceGeneratorSHA256> = mockk()
            override val serviceCredentials: Lazy<ServiceCredentials> = mockk()
            override val silentAuthServicesProvider: Lazy<SilentAuthServicesProvider> = mockk()
            override val trustedProvidersCache: Lazy<TrustedProvidersCache> = mockk()
            override val vkSilentAuthInfoProvider: Lazy<VkSilentAuthInfoProvider> = mockk()
            override val userDataFetcher: Lazy<UserDataFetcher> = mockk()
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
        var successAccessToken: AccessToken? = null
        val callback = object : VKID.AuthCallback {
            override fun onSuccess(accessToken: AccessToken) {
                successAccessToken = accessToken
            }

            override fun onFail(fail: VKIDAuthFail) = Unit
        }

        coEvery { authProvidersChooser.chooseBest(authParams) } returns authProvider
        every { authOptionsCreator.create(authParams) } returns authOptions
        every { authProvider.auth(context, authOptions) } just runs
        launch(testDispatcher) { vkid.authorize(authCallback = callback, authParams = authParams) }
        testDispatcher.scheduler.advanceUntilIdle()

        When("Auth is successful") {
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

            Then("Auth provider is selected") {
                coVerify { authProvider.auth(context, authOptions) }
            }

            Then("Parses result and notifier listener") {
                successAccessToken shouldBe AccessToken("TOKEN", 123L, expireTime)
            }
        }
    }
})
