package com.vk.id.onetap.xml

import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.onetap.base.ChangeAccountTest
import com.vk.id.onetap.common.OneTapOAuth
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.ONE_TAP)
@DisplayName("Смена аккаунта в XML OneTap")
public class OneTapChangeAccountXmlTest : ChangeAccountTest() {

    @Test
    @AllureId("2289913")
    @DisplayName("Успешное получение токена в XML OneTap смене аккаунта")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2289725")
    @DisplayName("Получение ошибочного редиректа в Activity в XML OneTap смене аккаунта")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2289667")
    @DisplayName("Получение ошибки отсутсвия браузера в XML OneTap смене аккаунта")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2289567")
    @DisplayName("Получение ошибки апи в XML OneTap смене аккаунта")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2289936")
    @DisplayName("Получение ошибки отмены авторизации в XML OneTap смене аккаунта")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2289809")
    @DisplayName("Получение ошибки отсутствия данных oauth в XML OneTap смене аккаунта")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2289901")
    @DisplayName("Получение ошибки отсутствия deviceId в XML OneTap смене аккаунта")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("2289684")
    @DisplayName("Получение ошибки неверного state в XML OneTap смене аккаунта")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    override fun setOneTapContent(
        vkid: VKID,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    ) {
        composeTestRule.activity.setContent(
            OneTap(composeTestRule.activity).apply {
                setCallbacks(
                    onAuth = onAuth,
                    onAuthCode = onAuthCode,
                    onFail = onFail
                )
                setVKID(vkid)
                isSignInToAnotherAccountEnabled = true
            }
        )
    }
}
