package com.vk.id.multibranding.xml

import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.multibranding.base.MultibrandingTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.xml.OneTap
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.MULTIBRANDING)
@DisplayName("Мультибрендинг в XML OneTap")
public class OneTapMultibrandingXmlTest(
    private val oAuth: OAuth,
) : MultibrandingTest(oAuth, skipTest = oAuth == OAuth.VK) {

    @Test
    @AllureId("1349201")
    @DisplayName("Успешное получение токена в XML OneTap Мультибрендинге")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("1349166")
    @DisplayName("Успешное получение токена после логаута в Compose OneTap")
    override fun tokenIsReceivedAfterFailedLogout() {
        super.tokenIsReceivedAfterFailedLogout()
    }

    @Test
    @AllureId("1349232")
    @DisplayName("Получение ошибочного редиректа в Activity в XML OneTap Мультибрендинге")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("1349055")
    @DisplayName("Получение ошибки отсутсвия браузера в XML OneTap Мультибрендинге")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("1349206")
    @DisplayName("Получение ошибки апи в XML OneTap Мультибрендинге")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("1349262")
    @DisplayName("Получение ошибки отмены авторизации в XML OneTap Мультибрендинге")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("1349247")
    @DisplayName("Получение ошибки отсутствия данных oauth в XML OneTap Мультибрендинге")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("1349037")
    @DisplayName("Получение ошибки отсутствия deviceId в XML OneTap Мультибрендинге")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("1349019")
    @DisplayName("Получение ошибки неверного state в XML OneTap Мультибрендинге")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    @Test
    @AllureId("1349153")
    @DisplayName("Успешное получение кода при схеме с бекендом в XML OneTap Мультибрендинге")
    override fun authCodeIsReceived() {
        super.authCodeIsReceived()
    }

    @Test
    @AllureId("1348763")
    @DisplayName("Получение ошибки загрузки пользовательских данных в Compose OneTap")
    override fun failedUserCallIsReceived() {
        super.failedUserCallIsReceived()
    }

    override fun setContent(
        onAuth: (OAuth?, AccessToken) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
        authParams: VKIDAuthUiParams,
    ) {
        composeTestRule.activity.setContent(
            OneTap(composeTestRule.activity).apply {
                setCallbacks(
                    onAuth = { oAuth, accessToken -> onAuth(oAuth?.toOAuth(), accessToken) },
                    onAuthCode = onAuthCode,
                    onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) },
                )
                oAuths = setOfNotNull(OneTapOAuth.fromOAuth(oAuth))
                this.authParams = authParams
            }
        )
    }
}
