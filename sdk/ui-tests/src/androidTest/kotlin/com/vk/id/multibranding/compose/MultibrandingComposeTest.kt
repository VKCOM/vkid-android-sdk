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
    @DisplayName(DISPLAY_NAME_TOKEN_IS_RECEIVED)
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2290746")
    @DisplayName(DISPLAY_NAME_FAILED_REDIRECT)
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2290712")
    @DisplayName(DISPLAY_NAME_NO_BROWSER)
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2290777")
    @DisplayName(DISPLAY_NAME_FAILED_API_CALL)
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2290690")
    @DisplayName(DISPLAY_NAME_CANCELLATION)
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2290739")
    @DisplayName(DISPLAY_NAME_FAILED_OAUTH)
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2290797")
    @DisplayName(DISPLAY_NAME_INVALID_UUID)
    override fun invalidUuidIsReceived() {
        super.invalidUuidIsReceived()
    }

    @Test
    @AllureId("2290770")
    @DisplayName(DISPLAY_NAME_INVALID_STATE)
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
