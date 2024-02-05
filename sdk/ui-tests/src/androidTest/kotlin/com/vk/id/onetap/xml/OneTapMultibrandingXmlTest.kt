package com.vk.id.onetap.xml

import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.multibranding.base.MultibrandingTest
import com.vk.id.onetap.common.OneTapOAuth
import io.qameta.allure.kotlin.junit4.DisplayName

@DisplayName("Multibranding auth in XML OneTap")
public class OneTapMultibrandingXmlTest(
    private val oAuth: OAuth,
) : MultibrandingTest(oAuth, skipTest = oAuth == OAuth.VK) {

    override fun setContent(
        vkid: VKID,
        onAuth: (OAuth?, AccessToken) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
    ) {
        composeTestRule.activity.setContent(
            OneTap(composeTestRule.activity).apply {
                setCallbacks(
                    onAuth = { oAuth, accessToken -> onAuth(oAuth?.toOAuth(), accessToken) },
                    onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) },
                )
                setVKID(vkid)
                oAuths = setOfNotNull(OneTapOAuth.fromOAuth(oAuth))
            }
        )
    }
}