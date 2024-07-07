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

@Platform(Platform.ANDROID_AUTO)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.MAKSIM_SPIRIDONOV)
@Priority(Priority.CRITICAL)
public abstract class BottomSheetFlowTest : BaseUiTest() {

    private companion object {
        val AUTH_CODE = AuthCodeData("d654574949e8664ba1")
    }

    @get:Rule
    public val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

//    @Before
//    public fun setAllureParam() //надо сделать

    public open fun authSuccessAfterRetry(): Unit = run {
        var receivedFail: VKIDAuthFail? = null
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        val oAuth: OAuth? = null
        before {
            vkidBuilder()
                .overrideDeviceIdToNull()
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
                .overrideDeviceIdToNull()
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
                    .overrideDeviceIdToNull()
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
                    composeTestRule.mainClock.advanceTimeBy(3000)
                    oneTapIsDisplayed()
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
                    composeTestRule.mainClock.advanceTimeBy(1000)
                    oneTapIsNotDisplayed()
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

    private fun TestContext<Unit>.oneTapIsDisplayed(): Unit =
        step("OneTap кнопка отображается, bottomsheet скрывается") {
            ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
                oneTapButton {
                    assertIsDisplayed()
                }
            }
        }

    private fun TestContext<Unit>.oneTapIsNotDisplayed(): Unit =
        step("OneTap кнопка не отображается, bottomsheet не скрывается") {
            ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
                oneTapButton {
                    performClick()
                }
            }
        }

    @OptIn(InternalVKIDApi::class)
    private fun vkidBuilder(): InternalVKIDTestBuilder = InternalVKIDTestBuilder(composeTestRule.activity)

    private fun TestContext<Unit>.continueAuth() = scenario(ContinueAuthScenario(composeTestRule))
}
