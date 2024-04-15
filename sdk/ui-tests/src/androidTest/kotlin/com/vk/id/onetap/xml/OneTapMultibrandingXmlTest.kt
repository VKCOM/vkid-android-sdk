package com.vk.id.onetap.xml

import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.multibranding.base.MultibrandingTest
import com.vk.id.onetap.common.OneTapOAuth
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.MULTIBRANDING)
@DisplayName("Мультибрендинг в XML OneTap")
public class OneTapMultibrandingXmlTest(
    private val oAuth: OAuth,
) : MultibrandingTest(oAuth, skipTest = oAuth == OAuth.VK) {

    @Test
    @AllureId("2290718")
    @DisplayName("Успешное получение токена в XML OneTap Мультибрендинге")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2290691")
    @DisplayName("Получение ошибочного редиректа в Activity в XML OneTap Мультибрендинге")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2290764")
    @DisplayName("Получение ошибки отсутсвия браузера в XML OneTap Мультибрендинге")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2290701")
    @DisplayName("Получение ошибки апи в XML OneTap Мультибрендинге")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2290684")
    @DisplayName("Получение ошибки отмены авторизации в XML OneTap Мультибрендинге")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2290750")
    @DisplayName("Получение ошибки отсутствия данных oauth в XML OneTap Мультибрендинге")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2290765")
    @DisplayName("Получение ошибки отсутствия deviceId в XML OneTap Мультибрендинге")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("2290805")
    @DisplayName("Получение ошибки неверного state в XML OneTap Мультибрендинге")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    override fun setContent(
        vkid: VKID,
        onAuth: (OAuth?, AccessToken) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
    ) {
        composeTestRule.activity.setContent(
            OneTap(composeTestRule.activity).apply {
                setCallbacks(
                    onAuth = { oAuth, accessToken -> onAuth(oAuth?.toOAuth(), accessToken) },
                    onAuthCode = onAuthCode,
                    onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) },
                )
                setVKID(vkid)
                oAuths = setOfNotNull(OneTapOAuth.fromOAuth(oAuth))
            }
        )
    }
}
