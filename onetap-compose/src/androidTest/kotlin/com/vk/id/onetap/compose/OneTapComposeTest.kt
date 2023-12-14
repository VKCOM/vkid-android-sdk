package com.vk.id.onetap.compose

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.OneTapTest
import com.vk.id.onetap.compose.onetap.OneTap
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
public class OneTapComposeTest : OneTapTest() {

    override fun setContent(
        onAuth: (AccessToken) -> Unit,
        onFail: (VKIDAuthFail) -> Unit,
    ) {
        composeTestRule.setContent {
            OneTap(
                onAuth = onAuth,
                onFail = onFail,
            )
        }
    }
}