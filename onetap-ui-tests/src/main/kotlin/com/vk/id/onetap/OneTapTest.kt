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
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.OneTapOAuth
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
    public val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<com.vk.id.common.AutoTestActivity>, com.vk.id.common.AutoTestActivity> =
        createAndroidComposeRule()

    @Test
    public fun tokenIsReceived(): Unit = run {
        var receivedOAuth: OneTapOAuth? = null
        var receivedToken: AccessToken? = null
        setContent(onAuth = { oAuth, token ->
            receivedOAuth = oAuth
            receivedToken = token
        })
        startAuth()
        waitForVkToStart()
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
        setContent(onFail = { oAuth, fail ->
            receivedOAuth = oAuth
            receivedFail = fail
        })
        startAuth()
        waitForVkToStart()
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

    public abstract fun setContent(
        onAuth: (OneTapOAuth?, AccessToken) -> Unit = { _, _ -> },
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
    )

    private fun TestContext<Unit>.startAuth(): Unit = step("Start auth") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            oneTapButton {
                performClick()
            }
        }
    }

    private fun TestContext<Unit>.waitForVkToStart() = step("Wait for vk start") {
        flakySafely {
            device.uiDevice.currentPackageName shouldBe "com.vkontakte.android"
        }
    }
}
