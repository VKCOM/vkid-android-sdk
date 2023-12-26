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
import com.vk.id.VKIDUser
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.common.AutoTestActivity
import com.vk.id.test.VKIDTestBuilder
import com.vk.id.test.VKIDTokenPayloadResponse
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
    private val oAuth: OAuth
) : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    public val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<AutoTestActivity>, AutoTestActivity> =
        createAndroidComposeRule()

    private companion object {
        private const val ACCESS_TOKEN = "access token"
        private const val EXPIRES_IN = 1000L
        private const val USER_ID = 0L
        private const val EMAIL = "email"
        private const val PHONE = "phone"
        private const val PHONE_ACCESS_KEY = "phone access key"
        private const val FIRST_NAME = "first name"
        private const val LAST_NAME = "last name"
        private const val AVATAR = "avatar"

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = OAuth.values()
    }

    @Test
    public fun tokenIsReceived(): Unit = run {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .user(VKIDUser(firstName = FIRST_NAME, lastName = LAST_NAME, photo200 = AVATAR))
            .build()
        setContent(
            vkid = vkid,
            onAuth = { oAuth, token ->
                receivedOAuth = oAuth
                accessToken = token
            },
        )
        startAuth()
        continueAuth()
        step("OAuth is received") {
            flakySafely {
                receivedOAuth shouldBe oAuth
            }
        }
        step("Token is received") {
            flakySafely {
                accessToken?.token shouldBe ACCESS_TOKEN
                accessToken?.userID shouldBe USER_ID
                accessToken?.userData shouldBe VKIDUser(
                    firstName = FIRST_NAME,
                    lastName = LAST_NAME,
                    phone = PHONE,
                    photo50 = null,
                    photo100 = null,
                    photo200 = AVATAR,
                    email = EMAIL
                )
            }
        }
    }

    @Test
    public fun failedRedirectActivityIsReceived(): Unit = run {
        var fail: VKIDAuthFail? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .notifyFailedRedirect()
            .build()
        setContent(
            vkid = vkid,
            onFail = { fail = it }
        )
        startAuth()
        continueAuth()
        step("Fail is received") {
            flakySafely {
                fail.shouldBeInstanceOf<VKIDAuthFail.FailedRedirectActivity>()
            }
        }
    }

    @Test
    public fun noBrowserAvailableIsReceived(): Unit = run {
        var fail: VKIDAuthFail? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .notifyNoBrowserAvailable()
            .build()
        setContent(
            vkid = vkid,
            onFail = { fail = it }
        )
        startAuth()
        continueAuth()
        step("Fail is received") {
            flakySafely {
                fail.shouldBeInstanceOf<VKIDAuthFail.NoBrowserAvailable>()
            }
        }
    }

    @Test
    public fun failedApiCallIsReceived(): Unit = run {
        var fail: VKIDAuthFail? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiError()
            .build()
        setContent(
            vkid = vkid,
            onFail = { fail = it }
        )
        startAuth()
        continueAuth()
        step("Fail is received") {
            flakySafely {
                fail.shouldBeInstanceOf<VKIDAuthFail.FailedApiCall>()
            }
        }
    }

    @Test
    public fun cancellationIsReceived(): Unit = run {
        var fail: VKIDAuthFail? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .build()
        setContent(
            vkid = vkid,
            onFail = { fail = it }
        )
        startAuth()
        step("Press back") {
            device.uiDevice.pressBack()
        }
        step("Fail is received") {
            flakySafely {
                fail.shouldBeInstanceOf<VKIDAuthFail.Canceled>()
            }
        }
    }

    @Test
    public fun failedOAuthIsReceived(): Unit = run {
        var fail: VKIDAuthFail? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .overrideOAuthToNull()
            .build()
        setContent(
            vkid = vkid,
            onFail = { fail = it }
        )
        startAuth()
        continueAuth()
        step("Fail is received") {
            flakySafely {
                fail.shouldBeInstanceOf<VKIDAuthFail.FailedOAuth>()
            }
        }
    }

    @Test
    public fun invalidUuidIsReceived(): Unit = run {
        var fail: VKIDAuthFail? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .overrideUuid("wrong uuid")
            .build()
        setContent(
            vkid = vkid,
            onFail = { fail = it }
        )
        startAuth()
        continueAuth()
        step("Fail is received") {
            flakySafely {
                fail shouldBe VKIDAuthFail.FailedOAuthState("Invalid uuid")
            }
        }
    }

    @Test
    public fun invalidStateIsReceived(): Unit = run {
        var fail: VKIDAuthFail? = null
        val vkid = VKIDTestBuilder(composeTestRule.activity)
            .mockApiSuccess()
            .overrideState("wrong state")
            .build()
        setContent(
            vkid = vkid,
            onFail = { fail = it }
        )
        startAuth()
        continueAuth()
        step("Fail is received") {
            flakySafely {
                fail shouldBe VKIDAuthFail.FailedOAuthState("Invalid state")
            }
        }
    }

    protected abstract fun setContent(
        vkid: VKID,
        onAuth: (OAuth, AccessToken) -> Unit = { _, _ -> },
        onFail: (VKIDAuthFail) -> Unit = {},
    )

    private fun VKIDTestBuilder.mockApiError() = getTokenResponse(
        Result.failure(UnsupportedOperationException("fake error"))
    )

    private fun VKIDTestBuilder.mockApiSuccess() = getTokenResponse(
        Result.success(
            VKIDTokenPayloadResponse(
                accessToken = ACCESS_TOKEN,
                expiresIn = EXPIRES_IN,
                userId = USER_ID,
                email = EMAIL,
                phone = PHONE,
                phoneAccessKey = PHONE_ACCESS_KEY,
            )
        )
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

    private fun TestContext<Unit>.continueAuth() = step("Wait for vk start") {
        ComposeScreen.onComposeScreen<AuthProviderScreen>(composeTestRule) {
            continueButton {
                performClick()
            }
        }
    }
}
