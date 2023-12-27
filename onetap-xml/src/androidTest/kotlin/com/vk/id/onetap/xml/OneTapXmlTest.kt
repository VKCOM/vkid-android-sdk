package com.vk.id.onetap.xml

import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth

public class OneTapXmlTest : OneTapTest() {

    override fun setContent(
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
    ) {
        composeTestRule.activity.setContent(
            OneTap(composeTestRule.activity).apply {
                setCallbacks(
                    onAuth = onAuth,
                    onFail = onFail
                )
            }
        )
    }
}