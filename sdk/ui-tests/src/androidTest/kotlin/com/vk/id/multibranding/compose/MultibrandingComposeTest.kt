package com.vk.id.multibranding.compose

import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.base.MultibrandingTest
import com.vk.id.multibranding.common.callback.OAuthListWidgetAuthCallback
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.MULTIBRANDING)
@DisplayName("Compose Мультибрендинг")
public class MultibrandingComposeTest(
    oAuth: OAuth
) : MultibrandingTest(oAuth) {

    @Test
    @AllureId("2290791")
    @DisplayName("Успешное получение токена в Compose Мультибрендинге")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2290746")
    @DisplayName("Получение ошибочного редиректа в Activity в Compose Мультибрендинге")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2290712")
    @DisplayName("Получение ошибки отсутсвия браузера в Compose Мультибрендинге")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2290777")
    @DisplayName("Получение ошибки апи в Compose Мультибрендинге")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2290690")
    @DisplayName("Получение ошибки отмены авторизации в Compose Мультибрендинге")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2290739")
    @DisplayName("Получение ошибки отсутствия данных oauth в Compose Мультибрендинге")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2290797")
    @DisplayName("Получение ошибки неверного uuid в Compose Мультибрендинге")
    override fun invalidUuidIsReceived() {
        super.invalidUuidIsReceived()
    }

    @Test
    @AllureId("2290770")
    @DisplayName("Получение ошибки неверного state в Compose Мультибрендинге")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    override fun setContent(
        vkid: VKID,
        onAuth: (OAuth?, AccessToken) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
    ) {
        composeTestRule.setContent {
            OAuthListWidget(
                vkid = vkid,
                onAuth = OAuthListWidgetAuthCallback.WithOAuth { oAuth, accessToken -> onAuth(oAuth, accessToken) },
                onFail = onFail,
            )
        }
    }
}
