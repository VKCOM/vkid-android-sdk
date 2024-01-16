package com.vk.id.onetap.compose

import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.OneTap

public class OneTapComposeTest : OneTapTest() {

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
            )
        }
    }
}