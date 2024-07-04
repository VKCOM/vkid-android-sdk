@file:OptIn(InternalVKIDApi::class)

package com.vk.id.bottomsheet.compose

import android.os.Handler
import android.os.Looper
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.bottomsheet.screen.BottomSheetRetryScreen
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.activity.AutoTestActivityRule
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.common.mockapi.MockApi
import com.vk.id.common.mockapi.mockApiError
import com.vk.id.common.mockapi.mockApiSuccess
import com.vk.id.common.mockprovider.ContinueAuthScenario
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import com.vk.id.onetap.screen.OneTapScreen
import com.vk.id.test.InternalVKIDTestBuilder
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Rule
import org.junit.Test

public class BottomSheetFlowComposeTest : BaseUiTest() {
    @get:Rule
    public val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

    private companion object {
        val AUTH_CODE = AuthCodeData("d654574949e8664ba1")
    }

    @Test
    @AllureId("2315339")
    @DisplayName("Успешная авторизация после ретрая в Compose BottomSheet")
    fun authSuccessAfterRetry(): Unit = run {
        var receivedFail: VKIDAuthFail? = null
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder().mockApiError().overrideDeviceIdToNull().build()
            setContent(onAuth = { oAuth, token ->
                receivedOAuth = oAuth
                accessToken = token
            }, onAuthCode = { authCode, isSuccess ->
                receivedAuthCode = authCode
                receivedAuthCodeSuccess = isSuccess
            }, onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }, autoHideOnSuccess = true
            )
        }.after {}.run {
            startAuth()
            continueAuth()
            step("Auth code не получен") {
                flakySafely {
                    receivedAuthCode.shouldBeNull()
                    receivedAuthCodeSuccess.shouldBeNull()
                }
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail shouldBe VKIDAuthFail.FailedRedirectActivity("No device id", null)
                    receivedOAuth shouldBe oAuth
                }
            }
            step("Нажимаем 'Попробовать снова'") {
                vkidBuilder().mockApiSuccess().user(MockApi.mockApiUser()).build()
                retryAuth()
            }
            continueAuth()
            step("Получен auth code") {
                flakySafely {
                    receivedAuthCode shouldBe AUTH_CODE
                    receivedAuthCodeSuccess shouldBe false
                }
            }
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

    @Test
    @AllureId("2315344")
    @DisplayName("Успешная смена аккаунта после ретрая в Compose BottomSheet")
    fun changeAccountSuccessAfterRetry() {
        var accessToken: AccessToken? = null
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder().notifyNoBrowserAvailable().build()
            setContent(onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }, onAuthCode = { authCode, isSuccess ->
                receivedAuthCode = authCode
                receivedAuthCodeSuccess = isSuccess
            }, onAuth = { oAuth, token ->
                receivedOAuth = oAuth
                accessToken = token
            }, autoHideOnSuccess = true
            )
        }.after {}.run {
            startAuth()
            step("Auth code не получен") {
                receivedAuthCode.shouldBeNull()
                receivedAuthCodeSuccess.shouldBeNull()
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.NoBrowserAvailable>()
                    receivedOAuth shouldBe oAuth
                }
            }
            step("Нажимаем 'Попробовать снова'") {
                vkidBuilder().mockApiSuccess().user(MockApi.mockApiUser()).build()
                retryAuth()
            }
            continueAuth()
            step("Получен auth code") {
                flakySafely {
                    receivedAuthCode shouldBe AUTH_CODE
                    receivedAuthCodeSuccess shouldBe false
                }
            }
            step("Получен OAuth") {
                flakySafely {
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    @Test
    @AllureId("2315335")
    @DisplayName("Ошибка авторизации после ретрая в Compose BottomSheet")
    fun authFailAfterRetry() {
    }

    @Test
    @AllureId("2315342")
    @DisplayName("Ошибка смены аккаунта после ретрая в Compose BottomSheet")
    fun changeAccountFailAfterRetry() {
    }

    @Test
    @AllureId("2315337")
    @DisplayName("Шторка не скрывается после авторизации в Compose BottomSheet")
    fun sheetNotHiddenAfterAuth() {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedFail: VKIDAuthFail? = null
        var receivedAuthCodeSuccess: Boolean? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder().mockApiSuccess().user(MockApi.mockApiUser()).build()
            setContent(onAuth = { oAuth, token ->
                receivedOAuth = oAuth
                accessToken = token
            }, onAuthCode = { authCode, isSuccess ->
                receivedAuthCode = authCode
                receivedAuthCodeSuccess = isSuccess
            }, onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }, autoHideOnSuccess = false
            )
        }.after {}.run {
            startAuth()
            continueAuth()
            step("Получен auth code") {
                flakySafely {
                    receivedAuthCode shouldBe AUTH_CODE
                    receivedAuthCodeSuccess = false
                }
            }
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
            step("Боттомшит скрылся после успешной авторизации") {
                flakySafely {
                    composeTestRule.onNodeWithTag("onetap_bottomsheet").assertIsDisplayed()
                }
            }
        }
    }

    @Test
    @AllureId("2315343")
    @DisplayName("Автоскрытие шторки в Compose BottomSheet")
    fun sheetAuthHide() {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        var receivedFail: VKIDAuthFail? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder().mockApiSuccess().user(MockApi.mockApiUser()).build()
            setContent(onAuth = { oAuth, token ->
                receivedOAuth = oAuth
                accessToken = token
            }, onAuthCode = { authCode, isSuccess ->
                receivedAuthCode = authCode
                receivedAuthCodeSuccess = isSuccess
            }, onFail = { oAuth, fail ->
                receivedFail = fail
                receivedOAuth = oAuth
            }, autoHideOnSuccess = true
            )
        }.after {}.run {
            startAuth()
            continueAuth()
            step("Получен auth code") {
                flakySafely {
                    receivedAuthCode shouldBe AUTH_CODE
                    receivedAuthCodeSuccess = false
                }
            }
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
            step("Боттомшит скрылся после успешной авторизации") {
                flakySafely {
                    composeTestRule.onNodeWithTag("onetap_bottomsheet").assertIsNotDisplayed()
                }
            }
        }
    }

    fun setContent(
        onAuth: (OAuth?, AccessToken) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
        autoHideOnSuccess: Boolean = true,
    ) {
        composeTestRule.setContent {
            val state = rememberOneTapBottomSheetState()
            OneTapBottomSheet(
                state = state,
                serviceName = "VK",
                onAuth = { oAuth, token -> onAuth(oAuth?.toOAuth(), token) },
                onAuthCode = onAuthCode,
                onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) },
                autoHideOnSuccess = autoHideOnSuccess
            )
            Handler(Looper.getMainLooper()).post {
                state.show()
            }
        }
    }

    fun TestContext<Unit>.startAuth(): Unit = step("Начало авторизации") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            oneTapButton {
                performClick()
            }
        }
    }

    protected fun TestContext<Unit>.retryAuth(): Unit = step("Повтор авторизации") {
        ComposeScreen.onComposeScreen<BottomSheetRetryScreen>(composeTestRule) {
            retryButton {
                performClick()
            }
        }
    }

    protected open fun vkidBuilder(): InternalVKIDTestBuilder = InternalVKIDTestBuilder(composeTestRule.activity)

    fun TestContext<Unit>.continueAuth() = scenario(ContinueAuthScenario(composeTestRule))

}
