package com.vk.id.multibranding.xml

import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.multibranding.common.callback.OAuthListWidgetAuthCallback
import com.vk.id.multibranding.ui.tests.MultibrandingTest

public class MultibrandingXmlTest(
    oAuth: OAuth
) : MultibrandingTest(oAuth) {

    override fun setContent(
        vkid: VKID,
        onAuth: (OAuth, AccessToken) -> Unit,
        onFail: (OAuth, VKIDAuthFail) -> Unit,
    ) {
        composeTestRule.activity.setContent(
            OAuthListWidget(composeTestRule.activity).apply {
                setCallbacks(
                    onAuth = OAuthListWidgetAuthCallback.WithOAuth { oAuth, accessToken -> onAuth(oAuth, accessToken) },
                    onFail = onFail,
                )
                setVKID(vkid)
            }
        )
    }
}
