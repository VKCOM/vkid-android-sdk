package com.vk.id.onetap.xml

import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.onetap.base.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.ONE_TAP)
@DisplayName("Авторизация в XML OneTap")
public class OneTapXmlTest : OneTapTest() {

    @Test
    @AllureId("2289750")
    @DisplayName("Успешное получение токена в XML OneTap")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2302987")
    @DisplayName("Успешное получение токена после логаута в XML OneTap")
    override fun tokenIsReceivedAfterFailedLogout() {
        super.tokenIsReceivedAfterFailedLogout()
    }

    @Test
    @AllureId("2289711")
    @DisplayName("Получение ошибочного редиректа в Activity в XML OneTap")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2289640")
    @DisplayName("Получение ошибки отсутсвия браузера в XML OneTap")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2289732")
    @DisplayName("Получение ошибки апи в XML OneTap")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2289561")
    @DisplayName("Получение ошибки отмены авторизации в XML OneTap")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2289553")
    @DisplayName("Получение ошибки отсутствия данных oauth в XML OneTap")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2289551")
    @DisplayName("Получение ошибки отсутствия deviceId в XML OneTap")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("2289691")
    @DisplayName("Получение ошибки неверного state в XML OneTap")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    @Test
    @AllureId("2303009")
    @DisplayName("Успешное получение кода при схеме с бекендом в XML OneTap Мультибрендинге")
    override fun authCodeIsReceived() {
        super.authCodeIsReceived()
    }

    @Test
    @AllureId("2302951")
    @DisplayName("Получение ошибки загрузки пользовательских данных в XML OneTap")
    override fun failedUserCallIsReceived() {
        super.failedUserCallIsReceived()
    }

    override fun setOneTapContent(
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
        authParams: VKIDAuthUiParams,
    ) {
        composeTestRule.activity.setContent(
            OneTap(composeTestRule.activity).apply {
                setCallbacks(
                    onAuth = onAuth,
                    onAuthCode = onAuthCode,
                    onFail = onFail
                )
                this.authParams = authParams
            }
        )
    }
}
