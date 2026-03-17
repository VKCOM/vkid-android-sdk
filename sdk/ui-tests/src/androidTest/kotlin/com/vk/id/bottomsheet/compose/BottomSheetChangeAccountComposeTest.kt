package com.vk.id.bottomsheet.compose

import android.os.Handler
import android.os.Looper
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.feature.TestFeature
import com.vk.id.onetap.base.ChangeAccountTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.BOTTOM_SHEET)
@DisplayName("Авторизация в Compose BottomSheet")
public class BottomSheetChangeAccountComposeTest : ChangeAccountTest() {

    @Test
    @AllureId("1349111")
    @DisplayName("Успешное получение токена в Compose BottomSheet смене аккаунта")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("1348704")
    @DisplayName("Успешное получение токена после логаута в Compose OneTap")
    override fun tokenIsReceivedAfterFailedLogout() {
        super.tokenIsReceivedAfterFailedLogout()
    }

    @Test
    @AllureId("1349315")
    @DisplayName("Получение ошибочного редиректа в Activity в Compose BottomSheet смене аккаунта")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("1348836")
    @DisplayName("Получение ошибки отсутсвия браузера в Compose BottomSheet смене аккаунта")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("1348842")
    @DisplayName("Получение ошибки апи в Compose BottomSheet смене аккаунта")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("1348873")
    @DisplayName("Получение ошибки отмены авторизации в Compose BottomSheet смене аккаунта")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("1349117")
    @DisplayName("Получение ошибки отсутствия данных oauth в Compose BottomSheet смене аккаунта")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("1348810")
    @DisplayName("Получение ошибки отсутствия deviceId в Compose BottomSheet смене аккаунта")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("1348955")
    @DisplayName("Получение ошибки неверного state в Compose BottomSheet смене аккаунта")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    @Test
    @AllureId("1348726")
    @DisplayName("Успешное получение кода при схеме с бекендом в XML OneTap Мультибрендинге")
    override fun authCodeIsReceived() {
        super.authCodeIsReceived()
    }

    @Test
    @AllureId("1348709")
    @DisplayName("Получение ошибки загрузки пользовательских данных в Compose OneTap")
    override fun failedUserCallIsReceived() {
        super.failedUserCallIsReceived()
    }

    override fun setOneTapContent(
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
        authParams: VKIDAuthUiParams,
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
}
