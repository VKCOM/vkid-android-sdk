package com.vk.id.onetap.compose

import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.onetap.base.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.onetap.OneTap
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.ONE_TAP)
@DisplayName("Авторизация в Compose OneTap Icon")
public class IconOneTapComposeTest : OneTapTest() {

    @Test
    @AllureId("2289707")
    @DisplayName("Успешное получение токена в Compose OneTap Icon")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2289931")
    @DisplayName("Получение ошибочного редиректа в Activity в Compose OneTap Icon")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2289726")
    @DisplayName("Получение ошибки отсутсвия браузера в Compose OneTap Icon")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2289841")
    @DisplayName("Получение ошибки апи в Compose OneTap Icon")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2289664")
    @DisplayName("Получение ошибки отмены авторизации в Compose OneTap Icon")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2289719")
    @DisplayName("Получение ошибки отсутствия данных oauth в Compose OneTap Icon")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2289922")
    @DisplayName("Получение ошибки отсутствия deviceId в Compose OneTap Icon")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("2289911")
    @DisplayName("Получение ошибки неверного state в Compose OneTap Icon")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    override fun setOneTapContent(
        vkid: VKID,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    ) {
        composeTestRule.setContent {
            OneTap(
                vkid = vkid,
                onAuth = onAuth,
                onFail = onFail,
                style = OneTapStyle.Icon()
            )
        }
    }
}
