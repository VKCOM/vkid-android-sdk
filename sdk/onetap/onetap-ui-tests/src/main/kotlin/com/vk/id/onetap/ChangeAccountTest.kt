@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap

import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.test.VKIDTestBuilder
import io.github.kakaocup.compose.node.element.ComposeScreen

public abstract class ChangeAccountTest : OneTapTest() {

    protected override fun vkidBuilder(): VKIDTestBuilder = super.vkidBuilder()
        .requireUnsetUseAuthProviderIfPossible()

    override fun TestContext<Unit>.startAuth() {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            signInToAnotherAccountButton {
                performClick()
            }
        }
    }
}
