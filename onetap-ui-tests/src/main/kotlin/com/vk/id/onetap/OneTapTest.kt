@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.common.activity.AutoTestActivityRule
import com.vk.id.common.mockapi.MockApi
import com.vk.id.common.mockapi.mockApiError
import com.vk.id.common.mockapi.mockApiSuccess
import com.vk.id.common.mockprovider.continueAuth
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.test.VKIDTestBuilder
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
public abstract class OneTapTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    public val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

    @Test
    public fun tokenIsReceived(): Unit = run {
        var receivedOAuth: OneTapOAuth? = null
        var receivedToken: AccessToken? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .user(MockApi.mockApiUser())
            .build()
        setContent(vkid) { oAuth, token ->
            receivedOAuth = oAuth
            receivedToken = token
        }
        startAuth()
        continueAuth(composeTestRule)
        step("Token is received") {
            flakySafely {
                receivedOAuth.shouldBeNull()
                receivedToken.shouldNotBeNull()
            }
        }
    }

    @Test
    public fun cancellationIsReceived(): Unit = run {
        var receivedOAuth: OneTapOAuth? = null
        var receivedFail: VKIDAuthFail? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity).build()
        setContent(vkid, onFail = { oAuth, fail ->
            receivedOAuth = oAuth
            receivedFail = fail
        })
        startAuth()
        step("Press back") {
            device.uiDevice.pressBack()
        }
        step("Cancellation fail is received") {
            flakySafely {
                receivedOAuth.shouldBeNull()
                receivedFail.shouldBeInstanceOf<VKIDAuthFail.Canceled>()
            }
        }
    }

    @Test
    public fun failedRedirectActivityIsReceived(): Unit = run {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OneTapOAuth? = null
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
                receivedOAuth.shouldBeNull()
            }
        }
    }

    @Test
    public fun noBrowserAvailableIsReceived(): Unit = run {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OneTapOAuth? = null
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
                receivedOAuth.shouldBeNull()
            }
        }
    }

    @Test
    public fun failedApiCallIsReceived(): Unit = run {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OneTapOAuth? = null
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
                receivedOAuth.shouldBeNull()
            }
        }
    }

    @Test
    public fun failedOAuthIsReceived(): Unit = run {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OneTapOAuth? = null
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
                receivedOAuth.shouldBeNull()
            }
        }
    }

    @Test
    public fun invalidUuidIsReceived(): Unit = run {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OneTapOAuth? = null
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
                receivedOAuth.shouldBeNull()
            }
        }
    }

    @Test
    public fun invalidStateIsReceived(): Unit = run {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OneTapOAuth? = null
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
                receivedOAuth.shouldBeNull()
            }
        }
    }

    public abstract fun setContent(
        vkid: VKID,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
        onAuth: (OneTapOAuth?, AccessToken) -> Unit = { _, _ -> },
    )

    private fun TestContext<Unit>.startAuth(): Unit = step("Start auth") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            oneTapButton {
                performClick()
            }
        }
    }
}
