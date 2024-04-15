package com.vk.id.multibranding.xml

import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.multibranding.base.MultibrandingTest
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.MULTIBRANDING)
@DisplayName("XML Мультибрендинг")
public class MultibrandingXmlTest(
    oAuth: OAuth
) : MultibrandingTest(oAuth) {

    @Test
    @AllureId("2290710")
    @DisplayName("Успешное получение токена в XML Мультибрендинге")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2290762")
    @DisplayName("Получение ошибочного редиректа в Activity в XML Мультибрендинге")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2290771")
    @DisplayName("Получение ошибки отсутсвия браузера в XML Мультибрендинге")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2290732")
    @DisplayName("Получение ошибки апи в XML Мультибрендинге")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2290817")
    @DisplayName("Получение ошибки отмены авторизации в XML Мультибрендинге")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2290811")
    @DisplayName("Получение ошибки отсутствия данных oauth в XML Мультибрендинге")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2290747")
    @DisplayName("Получение ошибки отсутствия deviceId в XML Мультибрендинге")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("2290709")
    @DisplayName("Получение ошибки неверного state в XML Мультибрендинге")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    @Test
    @AllureId("")
    @DisplayName("Успешное получение кода при схеме с бекендом в XML OneTap Мультибрендинге")
    override fun authCodeIsReceived() {
        super.authCodeIsReceived()
    }

    override fun setContent(
        vkid: VKID,
        onAuth: (OAuth?, AccessToken) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
        authParams: VKIDAuthUiParams,
    ) {
        composeTestRule.activity.setContent(
            OAuthListWidget(composeTestRule.activity).apply {
                setCallbacks(
                    onAuth = onAuth,
                    onAuthCode = onAuthCode,
                    onFail = onFail,
                )
                setVKID(vkid)
                this.authParams = authParams
            }
        )
    }
}
