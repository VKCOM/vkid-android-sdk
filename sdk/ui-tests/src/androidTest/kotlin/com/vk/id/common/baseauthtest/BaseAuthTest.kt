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
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Product
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.common.mockapi.MockApi
import com.vk.id.common.mockapi.mockApiError
import com.vk.id.common.mockapi.mockApiSuccess
import com.vk.id.common.mockprovider.ContinueAuthScenario
import com.vk.id.test.VKIDTestBuilder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.qameta.allure.kotlin.Allure
import org.junit.Before
import org.junit.Rule

@Platform("Android Manual")
@Product("VK ID SDK")
@Suppress("TooManyFunctions")
public abstract class BaseAuthTest(
    private val oAuth: OAuth?,
    private val skipTest: Boolean = false,
) : BaseUiTest() {

    @get:Rule
    public val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

    @Before
    public fun setAllureParam() {
        oAuth?.let { Allure.parameter("oauth", it.name) }
    }

    public open fun tokenIsReceived(): Unit = runIfShouldNotSkip {
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
            step("Получен OAuth") {
                flakySafely {
                    receivedOAuth shouldBe oAuth
                }
            }
            step("Получен токен") {
                flakySafely {
                    accessToken?.token shouldBe MockApi.ACCESS_TOKEN
                    accessToken?.userID shouldBe MockApi.USER_ID
                    accessToken?.userData shouldBe MockApi.mockReturnedUser()
                }
            }
        }
    }

    public open fun failedRedirectActivityIsReceived(): Unit = runIfShouldNotSkip {
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
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedRedirectActivity>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun noBrowserAvailableIsReceived(): Unit = runIfShouldNotSkip {
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
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.NoBrowserAvailable>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun failedApiCallIsReceived(): Unit = runIfShouldNotSkip {
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
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedApiCall>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun cancellationIsReceived(): Unit = runIfShouldNotSkip {
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
            step("Нажатие кнопки 'назад'") {
                device.uiDevice.pressBack()
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.Canceled>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun failedOAuthIsReceived(): Unit = runIfShouldNotSkip {
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
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedOAuth>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun invalidUuidIsReceived(): Unit = runIfShouldNotSkip {
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
            step("Получена ошибка") {
                flakySafely {
                    receivedFail shouldBe VKIDAuthFail.FailedOAuthState("Invalid uuid")
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun invalidStateIsReceived(): Unit = runIfShouldNotSkip {
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
            step("Получена ошибка") {
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

    internal companion object {
        const val DISPLAY_NAME_TOKEN_IS_RECEIVED = "Успешное получение токена"
        const val DISPLAY_NAME_FAILED_REDIRECT = "Получение ошибочного редиректа в Activity"
        const val DISPLAY_NAME_NO_BROWSER = "Получение ошибки отсутсвия браузера"
        const val DISPLAY_NAME_FAILED_API_CALL = "Получение ошибки апи"
        const val DISPLAY_NAME_CANCELLATION = "Получение ошибки отмены авторизации"
        const val DISPLAY_NAME_FAILED_OAUTH = "Получение ошибки отсутствия данных oauth"
        const val DISPLAY_NAME_INVALID_UUID = "Получение ошибки неверного uuid"
        const val DISPLAY_NAME_INVALID_STATE = "Получение ошибки неверного state"
    }
}
