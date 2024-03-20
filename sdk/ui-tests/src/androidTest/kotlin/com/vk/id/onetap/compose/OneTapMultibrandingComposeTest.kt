package com.vk.id.onetap.compose

import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.multibranding.base.MultibrandingTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.OneTap
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.MULTIBRANDING)
@DisplayName("Мультибрендинг в Compose OneTap")
public class OneTapMultibrandingComposeTest(
    private val oAuth: OAuth,
) : MultibrandingTest(oAuth, skipTest = oAuth == OAuth.VK) {

    @Test
    @AllureId("2290779")
    @DisplayName(DISPLAY_NAME_TOKEN_IS_RECEIVED)
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2290806")
    @DisplayName(DISPLAY_NAME_FAILED_REDIRECT)
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2290698")
    @DisplayName(DISPLAY_NAME_NO_BROWSER)
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2290700")
    @DisplayName(DISPLAY_NAME_FAILED_API_CALL)
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2290814")
    @DisplayName(DISPLAY_NAME_CANCELLATION)
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2290783")
    @DisplayName(DISPLAY_NAME_FAILED_OAUTH)
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2290798")
    @DisplayName(DISPLAY_NAME_INVALID_UUID)
    override fun invalidUuidIsReceived() {
        super.invalidUuidIsReceived()
    }

    @Test
    @AllureId("2290781")
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
            OneTap(
                vkid = vkid,
                onAuth = { oAuth, accessToken -> onAuth(oAuth?.toOAuth(), accessToken) },
                onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) },
                oAuths = setOfNotNull(OneTapOAuth.fromOAuth(oAuth))
            )
        }
    }
}
