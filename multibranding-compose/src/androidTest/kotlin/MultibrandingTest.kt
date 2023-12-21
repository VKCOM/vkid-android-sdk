@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap

import androidx.activity.ComponentActivity
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
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.common.callback.OAuthListWidgetAuthCallback
import com.vk.id.test.MockAuthProviderConfig
import com.vk.id.test.OverrideVKIDApi
import com.vk.id.test.VKIDTokenPayloadResponse
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

// TODO: Reuse for XML and Compose
@RunWith(Parameterized::class)
public class MultibrandingTest(
    private val oAuth: OAuth
) : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    public val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity> =
        createAndroidComposeRule()

    private companion object {
        private const val ACCESS_TOKEN = "access token"
        private const val EXPIRES_IN = 1000L
        private const val USER_ID = 0L
        private const val EMAIL = "email"
        private const val PHONE = "phone"
        private const val PHONE_ACCESS_KEY = "phone access key"

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = OAuth.values()
    }

    @Test
    public fun tokenIsReceived(): Unit = run {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        val api = mockApiSuccess()
        val vkid = VKID(composeTestRule.activity, api)
        setContent(
            vkid = vkid,
            onAuth = OAuthListWidgetAuthCallback.WithOAuth { oAuth, token ->
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
                accessToken?.userData?.email shouldBe EMAIL
                accessToken?.userData?.phone shouldBe PHONE
            }
        }
    }

    @Test
    public fun failedApiCallIsReceived(): Unit = run {
        var fail: VKIDAuthFail? = null
        val api = mockApiError()
        val vkid = VKID(composeTestRule.activity, api)
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
        val api = mockApiSuccess()
        val vkid = VKID(composeTestRule.activity, api)
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

    // TODO: Unify tests
    @Test
    public fun failedOAuthIsReceived(): Unit = run {
        var fail: VKIDAuthFail? = null
        val api = mockApiSuccess()
        val authProviderConfig = MockAuthProviderConfig(overrideOAuthToNull = true)
        val vkid = VKID(composeTestRule.activity, api, authProviderConfig)
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
        val api = mockApiSuccess()
        val authProviderConfig = MockAuthProviderConfig(overrideUuid = "wrong uuid")
        val vkid = VKID(composeTestRule.activity, api, authProviderConfig)
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
        val api = mockApiSuccess()
        val authProviderConfig = MockAuthProviderConfig(overrideState = "wrong state")
        val vkid = VKID(composeTestRule.activity, api, authProviderConfig)
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
    // TODO: Tests for other fails

    private fun setContent(
        vkid: VKID,
        onAuth: OAuthListWidgetAuthCallback = OAuthListWidgetAuthCallback.JustToken { },
        onFail: (VKIDAuthFail) -> Unit = {},
    ) {
        composeTestRule.setContent {
            OAuthListWidget(
                vkid = vkid,
                onAuth = onAuth,
                onFail = onFail,
            )
        }
    }

    private fun mockApiError() = object : OverrideVKIDApi {
        override fun getToken(
            code: String,
            codeVerifier: String,
            clientId: String,
            clientSecret: String,
            deviceId: String,
            redirectUri: String
        ) = Result.failure<VKIDTokenPayloadResponse>(UnsupportedOperationException("fake error"))
    }

    private fun mockApiSuccess() = object : OverrideVKIDApi {
        override fun getToken(
            code: String,
            codeVerifier: String,
            clientId: String,
            clientSecret: String,
            deviceId: String,
            redirectUri: String
        ) = Result.success(
            VKIDTokenPayloadResponse(
                accessToken = ACCESS_TOKEN,
                expiresIn = EXPIRES_IN,
                userId = USER_ID,
                email = EMAIL,
                phone = PHONE,
                phoneAccessKey = PHONE_ACCESS_KEY,
            )
        )
    }

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
