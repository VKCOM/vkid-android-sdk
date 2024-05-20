@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.base

import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.screen.OneTapScreen
import com.vk.id.test.InternalVKIDTestBuilder
import io.github.kakaocup.compose.node.element.ComposeScreen

public abstract class ChangeAccountTest : OneTapTest() {

    protected override fun vkidBuilder(): InternalVKIDTestBuilder = super.vkidBuilder()
        .requireUnsetUseAuthProviderIfPossible()

    override fun TestContext<Unit>.startAuth(): Unit = step("Начало авторизации") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            signInToAnotherAccountButton {
                performClick()
            }
        }
    }
}
