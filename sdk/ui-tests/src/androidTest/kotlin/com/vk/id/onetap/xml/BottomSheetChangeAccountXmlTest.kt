package com.vk.id.onetap.xml

import android.os.Handler
import android.os.Looper
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.base.ChangeAccountTest
import com.vk.id.onetap.common.OneTapOAuth
import io.qameta.allure.kotlin.junit4.DisplayName

@DisplayName("Change account in XML BottomSheet")
public class BottomSheetChangeAccountXmlTest : ChangeAccountTest() {

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
