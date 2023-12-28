@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.xml

import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.onetap.OneTapScreen
import com.vk.id.onetap.OneTapTest
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.test.VKIDTestBuilder
import io.github.kakaocup.compose.node.element.ComposeScreen

public class XmlChangeAccountComposeTest : OneTapTest() {

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

    override fun VKIDTestBuilder.mockUseAuthProviderIfPossible(): VKIDTestBuilder = requireUnsetUseAuthProviderIfPossible()

    override fun TestContext<Unit>.startAuth() {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            signInToAnotherAccountButton {
                performClick()
            }
        }
    }
}