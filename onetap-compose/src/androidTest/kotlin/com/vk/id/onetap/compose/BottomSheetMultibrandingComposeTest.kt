package com.vk.id.onetap.compose

import android.os.Handler
import android.os.Looper
import androidx.compose.material3.Button
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.multibranding.ui.tests.MultibrandingTest
import com.vk.id.onetap.ChangeAccountTest
import com.vk.id.onetap.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState

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