package com.vk.id.onetap.compose

import android.os.Handler
import android.os.Looper
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.feature.TestFeature
import com.vk.id.onetap.base.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.BOTTOM_SHEET)
@DisplayName("OneTap авторизация в Compose BottomSheet")
public class BottomSheetOneTapComposeTest : OneTapTest() {

    @Test
    @AllureId("2289875")
    @DisplayName("Успешное получение токена в Compose BottomSheet")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2289805")
    @DisplayName("Получение ошибочного редиректа в Activity в Compose BottomSheet")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2289878")
    @DisplayName("Получение ошибки отсутсвия браузера в Compose BottomSheet")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2289645")
    @DisplayName("Получение ошибки апи в Compose BottomSheet")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2289602")
    @DisplayName("Получение ошибки отмены авторизации в Compose BottomSheet")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2289968")
    @DisplayName("Получение ошибки отсутствия данных oauth в Compose BottomSheet")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2289894")
    @DisplayName("Получение ошибки отсутствия deviceId в Compose BottomSheet")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("2289701")
    @DisplayName("Получение ошибки неверного state в Compose BottomSheet")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    @Test
    @AllureId("")
    @DisplayName("Успешное получение кода при схеме с бекендом в XML OneTap Мультибрендинге")
    override fun authCodeIsReceived() {
        super.authCodeIsReceived()
    }

    override fun setOneTapContent(
        vkid: VKID,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
        authParams: VKIDAuthUiParams,
    ) {
        composeTestRule.setContent {
            val state = rememberOneTapBottomSheetState()
            OneTapBottomSheet(
                vkid = vkid,
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
