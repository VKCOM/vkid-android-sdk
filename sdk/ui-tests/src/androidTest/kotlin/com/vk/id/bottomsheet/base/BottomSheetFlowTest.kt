@file:OptIn(InternalVKIDApi::class)

package com.vk.id.bottomsheet.base

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.bottomsheet.screen.BottomSheetRetryScreen
import com.vk.id.bottomsheet.screen.BottomSheetScreen
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.activity.AutoTestActivityRule
import com.vk.id.common.allure.Owners
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Priority
import com.vk.id.common.allure.Product
import com.vk.id.common.allure.Project
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.common.mockapi.MockApi
import com.vk.id.common.mockapi.mockApiError
import com.vk.id.common.mockapi.mockApiSuccess
import com.vk.id.common.mockprovider.ContinueAuthScenario
import com.vk.id.onetap.screen.OneTapScreen
import com.vk.id.test.InternalVKIDTestBuilder
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.qameta.allure.kotlin.Owner
import org.junit.Rule
import java.util.UUID

@Platform(Platform.ANDROID_AUTO)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.MAKSIM_SPIRIDONOV)
@Priority(Priority.CRITICAL)
@Suppress("LongMethod")
public abstract class BottomSheetFlowTest : BaseUiTest() {

    private companion object {
        val DEVICE_ID = UUID.randomUUID().toString()
        val AUTH_CODE = AuthCodeData(code = "d654574949e8664ba1", deviceId = DEVICE_ID)
    }

    @get:Rule
    public val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

    public open fun authSuccessAfterRetry(): Unit = run {
        var receivedFail: VKIDAuthFail? = null
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder()
                .overrideDeviceId(null)
                .build()
            setContent(
                onAuth = { oAuth, token ->
                    receivedOAuth = oAuth
                    accessToken = token
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                autoHideOnSuccess = true
            )
        }.after {
        }.run {
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
            step("Делаем мок новых данных") {
                vkidBuilder()
                    .mockApiSuccess()
                    .user(MockApi.mockApiUser())
                    .overrideDeviceId(DEVICE_ID)
                    .build()
            }
            step("Нажимаем 'Попробовать снова'") {
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

    public open fun changeAccountSuccessAfterRetry(): Unit = run {
        var accessToken: AccessToken? = null
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder()
                .notifyNoBrowserAvailable()
                .requireUnsetUseAuthProviderIfPossible()
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
                onAuth = { oAuth, token ->
                    receivedOAuth = oAuth
                    accessToken = token
                },
                autoHideOnSuccess = true
            )
        }.after {
        }.run {
            changeAccount()
            step("Auth code не получен") {
                flakySafely {
                    receivedAuthCode.shouldBeNull()
                    receivedAuthCodeSuccess.shouldBeNull()
                }
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.NoBrowserAvailable>()
                    receivedOAuth shouldBe oAuth
                }
            }
            step("Делаем мок новых данных") {
                vkidBuilder()
                    .requireUnsetUseAuthProviderIfPossible()
                    .overrideDeviceId(DEVICE_ID)
                    .build()
            }
            step("Нажимаем 'Попробовать снова'") {
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

    public open fun authFailAfterRetry(): Unit = run {
        var receivedFail: VKIDAuthFail? = null
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder()
                .mockApiError()
                .overrideDeviceId(null)
                .build()
            setContent(
                onAuth = { oAuth, token ->
                    receivedOAuth = oAuth
                    accessToken = token
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                autoHideOnSuccess = true
            )
        }.after {
        }.run {
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
                retryAuth()
            }
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
            step("Получена ошибка с кнопкой Повторить") {
                retryButtonIsDisplayed()
            }
        }
    }

    public open fun changeAccountFailAfterRetry(): Unit = run {
        var accessToken: AccessToken? = null
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder()
                .notifyNoBrowserAvailable()
                .requireUnsetUseAuthProviderIfPossible()
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onAuth = { oAuth, token ->
                    receivedOAuth = oAuth
                    accessToken = token
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                autoHideOnSuccess = true
            )
        }.after {
        }.run {
            changeAccount()
            step("Auth code не получен") {
                flakySafely {
                    receivedAuthCode.shouldBeNull()
                    receivedAuthCodeSuccess.shouldBeNull()
                }
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.NoBrowserAvailable>()
                    receivedOAuth shouldBe oAuth
                }
            }
            step("Делаем мок новых данных") {
                vkidBuilder()
                    .overrideDeviceId(null)
                    .requireUnsetUseAuthProviderIfPossible()
                    .build()
            }
            step("Нажимаем 'Попробовать снова'") {
                retryAuth()
            }
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
        }
    }

    public open fun sheetNotHiddenAfterAuth(): Unit = run {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedFail: VKIDAuthFail? = null
        var receivedAuthCodeSuccess: Boolean? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder()
                .mockApiSuccess()
                .user(MockApi.mockApiUser())
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onAuth = { oAuth, token ->
                    receivedOAuth = oAuth
                    accessToken = token
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                autoHideOnSuccess = false
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
                    composeTestRule.mainClock.advanceTimeBy(1000)
                    bottomsheetIsDisplayed()
                }
            }
        }
    }

    public open fun sheetAuthHide(): Unit = run {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        var receivedFail: VKIDAuthFail? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder()
                .mockApiSuccess()
                .user(MockApi.mockApiUser())
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onAuth = { oAuth, token ->
                    receivedOAuth = oAuth
                    accessToken = token
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                autoHideOnSuccess = true
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
                    composeTestRule.mainClock.advanceTimeBy(3000)
                    bottomsheetIsNotDisplayed()
                }
            }
        }
    }

    abstract fun setContent(
        onAuth: (OAuth?, AccessToken) -> Unit = { _, _ -> },
        onAuthCode: (AuthCodeData, Boolean) -> Unit = { _, _ -> },
        onFail: (OAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
        authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
        autoHideOnSuccess: Boolean = true,
    )

    private fun TestContext<Unit>.changeAccount(): Unit = step("Начало авторизации") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            signInToAnotherAccountButton {
                performClick()
            }
        }
    }

    private fun TestContext<Unit>.startAuth(): Unit = step("Начало авторизации") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            oneTapButton {
                performClick()
            }
        }
    }

    private fun TestContext<Unit>.retryAuth(): Unit = step("Повтор авторизации") {
        ComposeScreen.onComposeScreen<BottomSheetRetryScreen>(composeTestRule) {
            retryButton {
                performClick()
            }
        }
    }

    private fun TestContext<Unit>.retryButtonIsDisplayed(): Unit = step("Проверяем наличие кнопки Повторить") {
        ComposeScreen.onComposeScreen<BottomSheetRetryScreen>(composeTestRule) {
            retryButton {
                assertIsDisplayed()
            }
        }
    }

    private fun TestContext<Unit>.bottomsheetIsNotDisplayed(): Unit =
        step("Bottomsheet не скрывается после успешной авторизации") {
            ComposeScreen.onComposeScreen<BottomSheetScreen>(composeTestRule) {
                bottomsheet {
                    assertIsNotDisplayed()
                }
            }
        }

    private fun TestContext<Unit>.bottomsheetIsDisplayed(): Unit =
        step("Bottomsheet скрывается после успешной авторизации") {
            ComposeScreen.onComposeScreen<BottomSheetScreen>(composeTestRule) {
                bottomsheet {
                    assertIsDisplayed()
                }
            }
        }

    @OptIn(InternalVKIDApi::class)
    private fun vkidBuilder(): InternalVKIDTestBuilder = InternalVKIDTestBuilder(composeTestRule.activity)

    private fun TestContext<Unit>.continueAuth() = scenario(ContinueAuthScenario(composeTestRule))
}
