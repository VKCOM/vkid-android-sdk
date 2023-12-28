@file:OptIn(InternalVKIDApi::class)

package com.vk.id.multibranding.ui.tests

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.common.AutoTestActivity
import com.vk.id.common.mockapi.MockApi
import com.vk.id.common.mockapi.mockApiError
import com.vk.id.common.mockapi.mockApiSuccess
import com.vk.id.common.mockprovider.continueAuth
import com.vk.id.test.VKIDTestBuilder
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
@Suppress("TooManyFunctions")
public abstract class MultibrandingTest(
    private val oAuth: OAuth,
    private val skipTest: Boolean = false,
) : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    public val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<AutoTestActivity>, AutoTestActivity> =
        createAndroidComposeRule()

    private companion object {

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = OAuth.values()
    }

    @Test
    public fun tokenIsReceived(): Unit = runIfShouldNotSkip {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .user(MockApi.mockApiUser())
            .build()
        setContent(
            vkid = vkid,
            onAuth = { oAuth, token ->
                receivedOAuth = oAuth
                accessToken = token
            },
        )
        startAuth()
        continueAuth(composeTestRule)
        step("OAuth is received") {
            flakySafely {
                receivedOAuth shouldBe oAuth
            }
        }
        step("Token is received") {
            flakySafely {
                accessToken?.token shouldBe MockApi.ACCESS_TOKEN
                accessToken?.userID shouldBe MockApi.USER_ID
                accessToken?.userData shouldBe MockApi.mockReturnedUser()
            }
        }
    }

    @Test
    public fun failedRedirectActivityIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .notifyFailedRedirect()
            .build()
        setContent(
            vkid = vkid,
            onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }
        )
        startAuth()
        continueAuth(composeTestRule)
        step("Fail is received") {
            flakySafely {
                receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedRedirectActivity>()
                receivedOAuth shouldBe oAuth
            }
        }
    }

    @Test
    public fun noBrowserAvailableIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .notifyNoBrowserAvailable()
            .build()
        setContent(
            vkid = vkid,
            onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }
        )
        startAuth()
        continueAuth(composeTestRule)
        step("Fail is received") {
            flakySafely {
                receivedFail.shouldBeInstanceOf<VKIDAuthFail.NoBrowserAvailable>()
                receivedOAuth shouldBe oAuth
            }
        }
    }

    @Test
    public fun failedApiCallIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiError()
            .build()
        setContent(
            vkid = vkid,
            onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }
        )
        startAuth()
        continueAuth(composeTestRule)
        step("Fail is received") {
            flakySafely {
                receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedApiCall>()
                receivedOAuth shouldBe oAuth
            }
        }
    }

    @Test
    public fun cancellationIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .build()
        setContent(
            vkid = vkid,
            onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }
        )
        startAuth()
        step("Press back") {
            device.uiDevice.pressBack()
        }
        step("Fail is received") {
            flakySafely {
                receivedFail.shouldBeInstanceOf<VKIDAuthFail.Canceled>()
                receivedOAuth shouldBe oAuth
            }
        }
    }

    @Test
    public fun failedOAuthIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .overrideOAuthToNull()
            .build()
        setContent(
            vkid = vkid,
            onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }
        )
        startAuth()
        continueAuth(composeTestRule)
        step("Fail is received") {
            flakySafely {
                receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedOAuth>()
                receivedOAuth shouldBe oAuth
            }
        }
    }

    @Test
    public fun invalidUuidIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .overrideUuid("wrong uuid")
            .build()
        setContent(
            vkid = vkid,
            onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }
        )
        startAuth()
        continueAuth(composeTestRule)
        step("Fail is received") {
            flakySafely {
                receivedFail shouldBe VKIDAuthFail.FailedOAuthState("Invalid uuid")
                receivedOAuth shouldBe oAuth
            }
        }
    }

    @Test
    public fun invalidStateIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .overrideState("wrong state")
            .build()
        setContent(
            vkid = vkid,
            onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }
        )
        startAuth()
        continueAuth(composeTestRule)
        step("Fail is received") {
            flakySafely {
                receivedFail shouldBe VKIDAuthFail.FailedOAuthState("Invalid state")
                receivedOAuth shouldBe oAuth
            }
        }
    }

    private fun runIfShouldNotSkip(
        test: TestContext<Unit>.() -> Unit,
    ) = run {
        if (!skipTest) {
            test()
        }
    }

    protected abstract fun setContent(
        vkid: VKID,
        onAuth: (OAuth?, AccessToken) -> Unit = { _, _ -> },
        onFail: (OAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
    )

    private fun TestContext<Unit>.startAuth(): Unit = step("Start auth") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            when (oAuth) {
                OAuth.VK -> vkButton.performClick()
                OAuth.MAIL -> mailButton.performClick()
                OAuth.OK -> okButton.performClick()
            }
        }
    }
}
