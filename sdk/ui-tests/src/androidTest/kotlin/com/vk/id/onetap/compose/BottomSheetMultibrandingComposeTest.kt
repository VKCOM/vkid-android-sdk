package com.vk.id.onetap.compose

import android.os.Handler
import android.os.Looper
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.common.feature.TestFeature
import com.vk.id.multibranding.base.MultibrandingTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.MULTIBRANDING)
@DisplayName("Мультибрендинг в Compose BottomSheet")
public class BottomSheetMultibrandingComposeTest(
    private val oAuth: OAuth,
) : MultibrandingTest(oAuth, skipTest = oAuth == OAuth.VK) {

    @Test
    @AllureId("2290716")
    @DisplayName(DISPLAY_NAME_TOKEN_IS_RECEIVED)
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2290755")
    @DisplayName(DISPLAY_NAME_FAILED_REDIRECT)
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2290680")
    @DisplayName(DISPLAY_NAME_NO_BROWSER)
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2290759")
    @DisplayName(DISPLAY_NAME_FAILED_API_CALL)
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2290715")
    @DisplayName(DISPLAY_NAME_CANCELLATION)
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2290818")
    @DisplayName(DISPLAY_NAME_FAILED_OAUTH)
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2290678")
    @DisplayName(DISPLAY_NAME_INVALID_UUID)
    override fun invalidUuidIsReceived() {
        super.invalidUuidIsReceived()
    }

    @Test
    @AllureId("2290683")
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
            val state = rememberOneTapBottomSheetState()
            OneTapBottomSheet(
                vkid = vkid,
                state = state,
                serviceName = "VK",
                onAuth = { oAuth, accessToken -> onAuth(oAuth?.toOAuth(), accessToken) },
                onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) },
                oAuths = setOfNotNull(OneTapOAuth.fromOAuth(oAuth))
            )
            Handler(Looper.getMainLooper()).post {
                state.show()
            }
        }
    }
}
