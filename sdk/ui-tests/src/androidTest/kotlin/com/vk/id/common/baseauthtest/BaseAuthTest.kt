@file:OptIn(InternalVKIDApi::class)

package com.vk.id.common.baseauthtest

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.activity.AutoTestActivityRule
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.common.mockapi.MockApi
import com.vk.id.common.mockapi.mockApiError
import com.vk.id.common.mockapi.mockApiSuccess
import com.vk.id.common.mockprovider.ContinueAuthScenario
import com.vk.id.test.VKIDTestBuilder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Rule
import org.junit.Test

@Suppress("TooManyFunctions")
public abstract class BaseAuthTest(
    private val oAuth: OAuth?,
    private val skipTest: Boolean = false,
) : BaseUiTest() {

    @get:Rule
    public val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

    @DisplayName("Test that token is received")
    @Test
    public fun tokenIsReceived(): Unit = runIfShouldNotSkip {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        before {
            val vkid = vkidBuilder()
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
        }.after {
        }.run {
            startAuth()
            continueAuth()
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
    }

    @DisplayName("Test that failed redirect activity is received")
    @Test
    public fun failedRedirectActivityIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        before {
            val vkid = vkidBuilder()
                .notifyFailedRedirect()
                .build()
            setContent(
                vkid = vkid,
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                }
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Fail is received") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedRedirectActivity>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    @DisplayName("Test that no browser available is received")
    @Test
    public fun noBrowserAvailableIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        before {
            val vkid = vkidBuilder()
                .notifyNoBrowserAvailable()
                .build()
            setContent(
                vkid = vkid,
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                }
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Fail is received") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.NoBrowserAvailable>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    @DisplayName("Test that failed api call is received")
    @Test
    public fun failedApiCallIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        before {
            val vkid = vkidBuilder()
                .mockApiError()
                .build()
            setContent(
                vkid = vkid,
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                }
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Fail is received") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedApiCall>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    @DisplayName("Test that cancellation is received")
    @Test
    public fun cancellationIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        before {
            val vkid = vkidBuilder()
                .mockApiSuccess()
                .build()
            setContent(
                vkid = vkid,
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                }
            )
        }.after {
        }.run {
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
    }

    @DisplayName("Test that failed oauth is received")
    @Test
    public fun failedOAuthIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        before {
            val vkid = vkidBuilder()
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
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Fail is received") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedOAuth>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    @DisplayName("Test that invalid uuid is received")
    @Test
    public fun invalidUuidIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        before {
            val vkid = vkidBuilder()
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
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Fail is received") {
                flakySafely {
                    receivedFail shouldBe VKIDAuthFail.FailedOAuthState("Invalid uuid")
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    @DisplayName("Test that invalid state is received")
    @Test
    public fun invalidStateIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        before {
            val vkid = vkidBuilder()
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
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Fail is received") {
                flakySafely {
                    receivedFail shouldBe VKIDAuthFail.FailedOAuthState("Invalid state")
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    private fun runIfShouldNotSkip(
        test: () -> Unit,
    ) {
        if (!skipTest) {
            test()
        }
    }

    protected abstract fun setContent(
        vkid: VKID,
        onAuth: (OAuth?, AccessToken) -> Unit = { _, _ -> },
        onFail: (OAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
    )

    private fun TestContext<Unit>.continueAuth() = scenario(ContinueAuthScenario(composeTestRule))

    protected abstract fun TestContext<Unit>.startAuth()

    protected open fun vkidBuilder(): VKIDTestBuilder = VKIDTestBuilder(composeTestRule.activity)
}
