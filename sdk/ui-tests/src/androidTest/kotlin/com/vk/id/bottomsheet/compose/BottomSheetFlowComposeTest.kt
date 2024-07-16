package com.vk.id.bottomsheet.compose

import android.os.Handler
import android.os.Looper
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.bottomsheet.base.BottomSheetFlowTest
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.BOTTOM_SHEET)
@DisplayName("Авторизация с помощью Bottomsheet")
@Suppress("LongMethod")
public class BottomSheetFlowComposeTest : BottomSheetFlowTest() {

    @Test
    @AllureId("2315339")
    @DisplayName("Успешная авторизация после ретрая в Compose BottomSheet")
    override fun authSuccessAfterRetry() {
        super.authSuccessAfterRetry()
    }

    @Test
    @AllureId("2315344")
    @DisplayName("Успешная смена аккаунта после ретрая в Compose BottomSheet")
    override fun changeAccountSuccessAfterRetry() {
        super.changeAccountSuccessAfterRetry()
    }

    @Test
    @AllureId("2315335")
    @DisplayName("Ошибка авторизации после ретрая в Compose BottomSheet")
    override fun authFailAfterRetry() {
        super.authFailAfterRetry()
    }

    @Test
    @AllureId("2315342")
    @DisplayName("Ошибка смены аккаунта после ретрая в Compose BottomSheet")
    override fun changeAccountFailAfterRetry() {
        super.changeAccountFailAfterRetry()
    }

    @Test
    @AllureId("2315337")
    @DisplayName("Шторка не скрывается после авторизации в Compose BottomSheet")
    override fun sheetNotHiddenAfterAuth() {
        super.sheetNotHiddenAfterAuth()
    }

    @Test
    @AllureId("2315343")
    @DisplayName("Автоскрытие шторки в Compose BottomSheet")
    override fun sheetAuthHide() {
        super.sheetAuthHide()
    }

    override fun setContent(
        onAuth: (OAuth?, AccessToken) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
        authParams: VKIDAuthUiParams,
        autoHideOnSuccess: Boolean
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
}
