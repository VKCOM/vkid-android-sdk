package com.vk.id.onetap.xml

import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.ChangeAccountTest
import com.vk.id.onetap.common.OneTapOAuth

public class XmlChangeAccountComposeTest : ChangeAccountTest() {

    override fun setContent(
        vkid: VKID,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    ) {
        composeTestRule.activity.setContent(
            OneTap(composeTestRule.activity).apply {
                setCallbacks(
                    onAuth = onAuth,
                    onFail = onFail
                )
                setVKID(vkid)
                isSignInToAnotherAccountEnabled = true
            }
        )
    }
}