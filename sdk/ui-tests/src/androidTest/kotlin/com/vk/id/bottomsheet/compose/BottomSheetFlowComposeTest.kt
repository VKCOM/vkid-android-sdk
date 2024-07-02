package com.vk.id.bottomsheet.compose

import android.os.Handler
import android.os.Looper
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.bottomsheet.screen.BottomSheetRetryScreen
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.mockapi.MockApi
import com.vk.id.common.mockapi.mockApiError
import com.vk.id.common.mockapi.mockApiSuccess
import com.vk.id.common.mockapi.mockGetTokenSuccess
import com.vk.id.onetap.base.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Suppress("EmptyFunctionBlock")
public class BottomSheetFlowComposeTest : OneTapTest() {
    private companion object {
        val AUTH_CODE = AuthCodeData("d654574949e8664ba1")
    }

    @OptIn(InternalVKIDApi::class)
    @Test
    @AllureId("2315339")
    @DisplayName("Успешная авторизация после ретрая в Compose BottomSheet")
    fun authSuccessAfterRetry(): Unit = runIfShouldNotSkip {
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
                vkidBuilder()
                    .mockApiSuccess()
                    .mockGetTokenSuccess()
                    .user(MockApi.mockApiUser())
                    .build()
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
    }

    @Test
    @AllureId("2315343")
    @DisplayName("Автоскрытие шторки в Compose BottomSheet")
    fun sheetAuthHide() {
    }

    override fun setOneTapContent(
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
        authParams: VKIDAuthUiParams
    ) {
        composeTestRule.setContent {
            val state = rememberOneTapBottomSheetState()
            OneTapBottomSheet(
                state = state,
                serviceName = "VK",
                onAuth = onAuth,
                onAuthCode = onAuthCode,
                onFail = onFail,
                authParams = authParams,
            )
            Handler(Looper.getMainLooper()).post {
                state.show()
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
}
