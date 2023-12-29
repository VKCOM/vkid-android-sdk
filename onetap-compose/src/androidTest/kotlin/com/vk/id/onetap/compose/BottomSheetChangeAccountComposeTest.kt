package com.vk.id.onetap.compose

import android.os.Handler
import android.os.Looper
import androidx.compose.material3.Button
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.ChangeAccountTest
import com.vk.id.onetap.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState

public class BottomSheetChangeAccountComposeTest : ChangeAccountTest() {

    override fun setContent(
        vkid: VKID,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    ) {
        composeTestRule.setContent {
            val state = rememberOneTapBottomSheetState()
            OneTapBottomSheet(
                vkid = vkid,
                state = state,
                serviceName = "VK",
                onAuth = onAuth,
                onFail = onFail,
            )
            Handler(Looper.getMainLooper()).post {
                state.show()
            }
        }
    }
}