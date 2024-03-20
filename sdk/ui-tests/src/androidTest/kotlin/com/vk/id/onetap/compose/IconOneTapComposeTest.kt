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
    @DisplayName(DISPLAY_NAME_TOKEN_IS_RECEIVED)
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2289931")
    @DisplayName(DISPLAY_NAME_FAILED_REDIRECT)
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2289726")
    @DisplayName(DISPLAY_NAME_NO_BROWSER)
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2289841")
    @DisplayName(DISPLAY_NAME_FAILED_API_CALL)
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2289664")
    @DisplayName(DISPLAY_NAME_CANCELLATION)
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2289719")
    @DisplayName(DISPLAY_NAME_FAILED_OAUTH)
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2289922")
    @DisplayName(DISPLAY_NAME_INVALID_UUID)
    override fun invalidUuidIsReceived() {
        super.invalidUuidIsReceived()
    }

    @Test
    @AllureId("2289911")
    @DisplayName(DISPLAY_NAME_INVALID_STATE)
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
