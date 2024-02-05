package com.vk.id.onetap.xml

import android.os.Handler
import android.os.Looper
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.base.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import io.qameta.allure.kotlin.junit4.DisplayName

@DisplayName("OneTap auth in XML BottomSheet")
public class BottomSheetOneTapXmlTest : OneTapTest() {

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