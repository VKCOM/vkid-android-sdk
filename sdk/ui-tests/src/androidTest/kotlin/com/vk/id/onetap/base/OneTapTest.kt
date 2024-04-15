package com.vk.id.onetap.base

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
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
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
    ): Unit = setOneTapContent(
        vkid = vkid,
        onAuth = { oAuth, token -> onAuth(oAuth?.toOAuth(), token) },
        onAuthCode = onAuthCode,
        onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) }
    )

    public abstract fun setOneTapContent(
        vkid: VKID,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onAuth: (OneTapOAuth?, AccessToken) -> Unit = { _, _ -> },
    )

    protected override fun TestContext<Unit>.startAuth(): Unit = step("Начало авторизации") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            oneTapButton {
                performClick()
            }
        }
    }
}
