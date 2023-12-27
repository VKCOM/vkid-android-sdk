package com.vk.id.onetap.compose

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.onetap.OneTap
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
public class IconOneTapComposeTest : OneTapTest() {

    override fun setContent(
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
    ) {
        composeTestRule.setContent {
            OneTap(
                onAuth = onAuth,
                onFail = onFail,
                style = OneTapStyle.Icon()
            )
        }
    }
}