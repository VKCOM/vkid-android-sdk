@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.base

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.baseauthtest.BaseAuthTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.screen.OneTapScreen
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
public abstract class OneTapTest : BaseAuthTest(
    oAuth = null,
    skipTest = false
) {

    override fun setContent(
        vkid: VKID,
        onAuth: (OAuth?, AccessToken) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
    ): Unit = setOneTapContent(
        vkid = vkid,
        onAuth = { oAuth, token -> onAuth(oAuth?.toOAuth(), token) },
        onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) }
    )

    public abstract fun setOneTapContent(
        vkid: VKID,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
        onAuth: (OneTapOAuth?, AccessToken) -> Unit = { _, _ -> },
    )

    protected override fun TestContext<Unit>.startAuth(): Unit = step("Start auth") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            oneTapButton {
                performClick()
            }
        }
    }
}
