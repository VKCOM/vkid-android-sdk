package com.vk.id.onetap.xml

import android.os.Handler
import android.os.Looper
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.onetap.base.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.BOTTOM_SHEET)
@DisplayName("OneTap авторизация в XML BottomSheet")
public class BottomSheetOneTapXmlTest : OneTapTest() {

    @Test
    @AllureId("2289769")
    @DisplayName(DISPLAY_NAME_TOKEN_IS_RECEIVED)
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2289575")
    @DisplayName(DISPLAY_NAME_FAILED_REDIRECT)
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2289641")
    @DisplayName(DISPLAY_NAME_NO_BROWSER)
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2289779")
    @DisplayName(DISPLAY_NAME_FAILED_API_CALL)
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2289630")
    @DisplayName(DISPLAY_NAME_CANCELLATION)
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2289643")
    @DisplayName(DISPLAY_NAME_FAILED_OAUTH)
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2289677")
    @DisplayName(DISPLAY_NAME_INVALID_UUID)
    override fun invalidUuidIsReceived() {
        super.invalidUuidIsReceived()
    }

    @Test
    @AllureId("2289655")
    @DisplayName(DISPLAY_NAME_INVALID_STATE)
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    override fun setOneTapContent(
        vkid: VKID,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    ) {
        val view = OneTapBottomSheet(composeTestRule.activity).apply {
            setCallbacks(
                onAuth = onAuth,
                onFail = onFail
            )
            setVKID(vkid)
        }
        composeTestRule.activity.setContent(view)
        Handler(Looper.getMainLooper()).post { view.show() }
    }
}
