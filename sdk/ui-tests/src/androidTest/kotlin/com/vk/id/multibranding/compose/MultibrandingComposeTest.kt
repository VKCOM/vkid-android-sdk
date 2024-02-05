package com.vk.id.multibranding.compose

import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.common.callback.OAuthListWidgetAuthCallback
import com.vk.id.multibranding.base.MultibrandingTest
import io.qameta.allure.kotlin.junit4.DisplayName

@DisplayName("Compose multibranding auth")
public class MultibrandingComposeTest(
    oAuth: OAuth
) : MultibrandingTest(oAuth) {

    override fun setContent(
        vkid: VKID,
        onAuth: (OAuth?, AccessToken) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
    ) {
        composeTestRule.setContent {
            OAuthListWidget(
                vkid = vkid,
                onAuth = OAuthListWidgetAuthCallback.WithOAuth { oAuth, accessToken -> onAuth(oAuth, accessToken) },
                onFail = onFail,
            )
        }
    }
}
