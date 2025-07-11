@file:OptIn(InternalVKIDApi::class)

package com.vk.id

import android.content.Context
import android.os.Looper
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.analytics.stat.StatTracker
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.exchangetoken.VKIDTokenExchanger
import com.vk.id.groupsubscription.GroupSubscriptionLimit
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.context.InternalVKIDActivityStarter
import com.vk.id.internal.context.InternalVKIDPackageManager
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.ipc.VkSilentAuthInfoProvider
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.logout.VKIDLoggerOut
import com.vk.id.network.InternalVKIDApiContract
import com.vk.id.network.groupsubscription.InternalVKIDGroupSubscriptionApiService
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.refreshuser.VKIDUserRefresher
import com.vk.id.storage.InternalVKIDEncryptedSharedPreferencesStorage
import com.vk.id.storage.InternalVKIDTokenStorage
import com.vk.id.tracking.core.CrashReporter
import com.vk.id.tracking.core.PerformanceTracker
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
internal class VKIDTest : BehaviorSpec({

    coroutineTestScope = true

    val authProvidersChooser = mockk<AuthProvidersChooser>()
    val authProvider = mockk<VKIDAuthProvider>()
    val authOptionsCreator = mockk<AuthOptionsCreator>()
    val authCallbacksHolder = mockk<AuthCallbacksHolder>()
    val authResultHandler = mockk<AuthResultHandler>()
    val userDataFetcher = mockk<UserDataFetcher>()
    val dispatchers = mockk<VKIDCoroutinesDispatchers>()
    val statTracker = mockk<StatTracker>(relaxed = true)
    var isFlutter = false
    val serviceCredentials = mockk<ServiceCredentials>()
    every { serviceCredentials.clientID } returns "1"
    val crashReporter = mockk<CrashReporter>()
    val performanceTracker = mockk<PerformanceTracker>()
    val appContext = mockk<Context>()
    val context = mockk<Context>()
    every { context.applicationContext } returns appContext
    every { crashReporter.report(any()) } just runs
    every { crashReporter.runReportingCrashes<Unit>(any(), any()) } answers {
        secondArg<() -> Unit>()()
    }
    coEvery { crashReporter.runReportingCrashesSuspend<Any>(any(), any()) } coAnswers {
        secondArg<suspend () -> Any>()()
    }
    mockkStatic(Looper::class)
    val looper = mockk<Looper> {
        every { thread } returns Thread.currentThread()
    }
    every { Looper.getMainLooper() } returns looper
    val trackingTracker = mockk<VKIDAnalytics.Tracker>(relaxed = true)
    val deps = object : VKIDDeps {
        override val groupSubscriptionLimit: GroupSubscriptionLimit? = null
        override val context: Context = mockk()
        override val appContext: Context = context
        override val authProvidersChooser: Lazy<AuthProvidersChooser> = lazy { authProvidersChooser }
        override val authOptionsCreator: AuthOptionsCreator = authOptionsCreator
        override val serviceCredentials: Lazy<ServiceCredentials> = lazy { serviceCredentials }
        override val authCallbacksHolder: AuthCallbacksHolder = authCallbacksHolder
        override val authResultHandler: Lazy<AuthResultHandler> = lazy { authResultHandler }
        override val dispatchers: VKIDCoroutinesDispatchers = dispatchers
        override val statTracker: StatTracker = statTracker
        override val vkSilentAuthInfoProvider: Lazy<VkSilentAuthInfoProvider> = mockk()
        override val userDataFetcher: Lazy<UserDataFetcher> = lazy { userDataFetcher }
        override val api: Lazy<InternalVKIDApiContract> = lazy { mockk() }
        override val tokenRefresher: Lazy<VKIDTokenRefresher> = lazy { mockk() }
        override val tokenExchanger: Lazy<VKIDTokenExchanger> = lazy { mockk() }
        override val userRefresher: Lazy<VKIDUserRefresher> = lazy { mockk() }
        override val loggerOut: Lazy<VKIDLoggerOut> = lazy { mockk() }
        override val tokenStorage: InternalVKIDTokenStorage = mockk()
        override val deviceIdStorage: Lazy<InternalVKIDDeviceIdProvider.DeviceIdStorage> = lazy { mockk() }
        override val prefsStore: Lazy<InternalVKIDPrefsStore> = lazy { mockk() }
        override val encryptedSharedPreferencesStorage: Lazy<InternalVKIDEncryptedSharedPreferencesStorage> =
            lazy { mockk() }
        override val vkidPackageManager: InternalVKIDPackageManager = mockk()
        override val activityStarter: InternalVKIDActivityStarter = mockk()
        override val isFlutter: Boolean
            get() = isFlutter
        override val crashReporter: CrashReporter = crashReporter
        override val performanceTracker: PerformanceTracker = performanceTracker
        override val trackingTracker: VKIDAnalytics.Tracker = trackingTracker
        override val groupSubscriptionApiService: Lazy<InternalVKIDGroupSubscriptionApiService>
            get() = lazy { mockk() }
    }
    val sdkInitEvent = "vkid_sdk_init"
    val flutterParam = VKIDAnalytics.EventParam("wrapper_sdk_type", strValue = "flutter")
    val limitParam = VKIDAnalytics.EventParam("limit_settings", strValue = null)

    Given("VKID for flutter SDK") {
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        every { dispatchers.io } returns testDispatcher
        isFlutter = true
        VKID(deps)

        When("VKID initialized") {
            Then("Analytics vkid_sdk_init event is sent") {
                verify {
                    statTracker.trackEvent(
                        null,
                        sdkInitEvent,
                        flutterParam,
                        limitParam,
                    )
                }
                verify {
                    trackingTracker.trackEvent(
                        null,
                        sdkInitEvent,
                        flutterParam,
                        limitParam,
                    )
                }
            }
        }
    }

    Given("Auth with VK is called") {
        val scheduler = testCoroutineScheduler
        val testDispatcher = StandardTestDispatcher(scheduler)
        every { dispatchers.io } returns testDispatcher
        isFlutter = false
        val vkid = VKID(deps)

        When("VKID initialized") {
            Then("Analytics vkid_sdk_init event is sent") {
                verify { statTracker.trackEvent(null, sdkInitEvent, flutterParam, limitParam) }
                verify { trackingTracker.trackEvent(null, sdkInitEvent, flutterParam, limitParam) }
            }

            val authParams = VKIDAuthParams { oAuth = OAuth.VK }
            val authOptions = AuthOptions(
                appId = "appId",
                codeChallenge = "code challenge",
                codeChallengeMethod = "code challenge method",
                redirectUriBrowser = "redirect uri browser",
                redirectUriCodeFlow = "redirect uri provider",
                state = "state",
                locale = "locale",
                theme = "theme",
                webAuthPhoneScreen = false,
                oAuth = null,
                prompt = "",
                scopes = emptySet(),
                statsInfo = "",
                sdkVersion = "1"
            )
            coEvery { authProvidersChooser.chooseBest(authParams) } returns authProvider
            every { authOptionsCreator.create(authParams, any()) } returns authOptions
            every { authProvider.auth(authOptions) } just runs
            every { authCallbacksHolder.add(any()) } just runs
            every { performanceTracker.startTracking("Authorize") } just runs
            every { performanceTracker.endTracking("Authorize") } just runs
            coEvery { authResultHandler.handle(any(), any()) } just runs
            runTest(scheduler) {
                vkid.authorize(callback = mockk(), params = authParams)
            }

            When("Auth result is delivered") {
                runTest(scheduler) {
                    AuthEventBridge.listener?.onAuthResult(
                        AuthResult.Success(
                            oauth = null,
                            "device id"
                        )
                    )
                }

                Then("Starts measuring authorize performance") {
                    verify { performanceTracker.startTracking("Authorize") }
                }

                Then("Auth result is handled") {
                    coVerify { authResultHandler.handle(any(), any()) }
                }

                Then("Auth callback is added") {
                    verify { authCallbacksHolder.add(any()) }
                }

                Then("Auth provider is selected") {
                    coVerify { authProvider.auth(authOptions) }
                }

                Then("Ends measuring authorize performance") {
                    verify { performanceTracker.endTracking("Authorize") }
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
    }
})
