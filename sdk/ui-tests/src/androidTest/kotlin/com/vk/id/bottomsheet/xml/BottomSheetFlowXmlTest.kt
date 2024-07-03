package com.vk.id.bottomsheet.xml

import android.os.Handler
import android.os.Looper
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.bottomsheet.base.BottomSheetFlowTest
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import com.vk.id.onetap.screen.OneTapScreen
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Suppress("EmptyFunctionBlock")
public class BottomSheetFlowXmlTest : BottomSheetFlowTest(
    oAuth = null,
) {

    @Test
    @AllureId("2315336")
    @DisplayName("Успешная авторизация после ретрая в XML BottomSheet")
    override fun authSuccessAfterRetry() {
    }

    @Test
    @AllureId("2315340")
    @DisplayName("Успешная смена аккаунта после ретрая в XML BottomSheet")
    override fun changeAccountSuccessAfterRetry() {
    }

    @Test
    @AllureId("2315346")
    @DisplayName("Ошибка авторизации после ретрая в XML BottomSheet")
    override fun authFailAfterRetry() {
    }

    @Test
    @AllureId("2315341")
    @DisplayName("Ошибка смены аккаунта после ретрая в XML BottomSheet")
    override fun changeAccountFailAfterRetry() {
    }

    @Test
    @AllureId("2315347")
    @DisplayName("Шторка не скрывается после авторизации в XML BottomSheet")
    override fun sheetNotHiddenAfterAuth() {
    }

    @Test
    @AllureId("2315345")
    @DisplayName("Автоскрытие шторки в XML BottomSheet")
    override fun sheetAuthHide() {
    }

    override fun setContent(
        onAuth: (OAuth?, AccessToken) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
        authParams: VKIDAuthUiParams
    ) {
        composeTestRule.setContent {
            val state = rememberOneTapBottomSheetState()
            OneTapBottomSheet(
                state = state,
                serviceName = "VK",
                onAuth = { oAuth, token -> onAuth(oAuth?.toOAuth(), token) },
                onAuthCode = onAuthCode,
                onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) },
                authParams = authParams,
            )
            Handler(Looper.getMainLooper()).post {
                state.show()
            }
        }
    }

    override fun TestContext<Unit>.startAuth(): Unit = step("Начало авторизации") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            oneTapButton {
                performClick()
            }
        }
    }
}
