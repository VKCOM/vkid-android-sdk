package com.vk.id.onetap.compose

import android.os.Handler
import android.os.Looper
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.multibranding.base.MultibrandingTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import io.qameta.allure.kotlin.junit4.DisplayName

@DisplayName("Multibranding auth in Compose BottomSheet")
public class BottomSheetMultibrandingComposeTest(
    private val oAuth: OAuth,
) : MultibrandingTest(oAuth, skipTest = oAuth == OAuth.VK) {

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
