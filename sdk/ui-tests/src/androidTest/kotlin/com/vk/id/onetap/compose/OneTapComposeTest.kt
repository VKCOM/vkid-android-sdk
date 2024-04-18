package com.vk.id.onetap.compose

import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.feature.TestFeature
import com.vk.id.onetap.base.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.OneTap
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.ONE_TAP)
@DisplayName("Авторизация в Compose OneTap")
public class OneTapComposeTest : OneTapTest() {

    @Test
    @AllureId("2289734")
    @DisplayName("Успешное получение токена в Compose OneTap")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2302952")
    @DisplayName("Успешное получение токена после логаута в Compose OneTap")
    override fun tokenIsReceivedAfterFailedLogout() {
        super.tokenIsReceivedAfterFailedLogout()
    }

    @Test
    @AllureId("2289585")
    @DisplayName("Получение ошибочного редиректа в Activity в Compose OneTap")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2289751")
    @DisplayName("Получение ошибки отсутсвия браузера в Compose OneTap")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2289676")
    @DisplayName("Получение ошибки апи в Compose OneTap")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2289715")
    @DisplayName("Получение ошибки отмены авторизации в Compose OneTap")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2289682")
    @DisplayName("Получение ошибки отсутствия данных oauth в Compose OneTap")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2289844")
    @DisplayName("Получение ошибки отсутствия deviceId в Compose OneTap")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("2289642")
    @DisplayName("Получение ошибки неверного state в Compose OneTap")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    @Test
    @AllureId("2302976")
    @DisplayName("Успешное получение кода при схеме с бекендом в XML OneTap Мультибрендинге")
    override fun authCodeIsReceived() {
        super.authCodeIsReceived()
    }

    @Test
    @AllureId("2303016")
    @DisplayName("Получение ошибки загрузки пользовательских данных в Compose OneTap")
    override fun failedUserCallIsReceived() {
        super.failedUserCallIsReceived()
    }

    override fun setOneTapContent(
        vkid: VKID,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
        authParams: VKIDAuthUiParams,
    ) {
        composeTestRule.setContent {
            OneTap(
                vkid = vkid,
                onAuth = onAuth,
                onAuthCode = onAuthCode,
                onFail = onFail,
                authParams = authParams,
            )
        }
    }
}
