package com.vk.id.multibranding.compose

import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.base.MultibrandingTest
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.MULTIBRANDING)
@DisplayName("Compose Мультибрендинг")
public class MultibrandingComposeTest(
    oAuth: OAuth
) : MultibrandingTest(oAuth) {

    @Test
    @AllureId("1349025")
    @DisplayName("Успешное получение токена в Compose Мультибрендинге")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("1348693")
    @DisplayName("Успешное получение токена после логаута в Compose OneTap")
    override fun tokenIsReceivedAfterFailedLogout() {
        super.tokenIsReceivedAfterFailedLogout()
    }

    @Test
    @AllureId("1349252")
    @DisplayName("Получение ошибочного редиректа в Activity в Compose Мультибрендинге")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("1349178")
    @DisplayName("Получение ошибки отсутсвия браузера в Compose Мультибрендинге")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("1349114")
    @DisplayName("Получение ошибки апи в Compose Мультибрендинге")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("1349242")
    @DisplayName("Получение ошибки отмены авторизации в Compose Мультибрендинге")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("1349172")
    @DisplayName("Получение ошибки отсутствия данных oauth в Compose Мультибрендинге")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("1349106")
    @DisplayName("Получение ошибки отсутствия deviceId в Compose Мультибрендинге")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("1349031")
    @DisplayName("Получение ошибки неверного state в Compose Мультибрендинге")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    @Test
    @AllureId("1349211")
    @DisplayName("Успешное получение кода при схеме с бекендом в XML OneTap Мультибрендинге")
    override fun authCodeIsReceived() {
        super.authCodeIsReceived()
    }

    @Test
    @AllureId("1348616")
    @DisplayName("Получение ошибки загрузки пользовательских данных в Compose OneTap")
    override fun failedUserCallIsReceived() {
        super.failedUserCallIsReceived()
    }

    override val expectedUriParams: Map<String, String> =
        super.expectedUriParams.toMutableMap().apply {
            put("scheme", "space_gray")
        }

    override fun setContent(
        onAuth: (OAuth?, AccessToken) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
        authParams: VKIDAuthUiParams,
    ) {
        composeTestRule.setContent {
            OAuthListWidget(
                onAuth = onAuth,
                onAuthCode = onAuthCode,
                onFail = onFail,
                authParams = authParams,
            )
        }
    }
}
