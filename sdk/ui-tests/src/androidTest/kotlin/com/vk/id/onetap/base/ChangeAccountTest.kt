@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.base

import android.net.Uri
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.screen.OneTapScreen
import com.vk.id.test.InternalVKIDTestBuilder
import com.vk.id.util.shouldHaveParameter
import io.github.kakaocup.compose.node.element.ComposeScreen

public abstract class ChangeAccountTest : OneTapTest() {

    protected override fun vkidBuilder(): InternalVKIDTestBuilder {
        requireUnsetUseAuthProviderIfPossible()
        return super.vkidBuilder()
    }

    override fun TestContext<Unit>.startAuth(): Unit = step("Начало авторизации") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            signInToAnotherAccountButton {
                performClick()
            }
        }
    }

    override val supportedUriParams: Set<String>
        get() = super.supportedUriParams.toMutableSet().apply {
            add("screen")
        }

    protected override fun checkProviderReceivedUri(providerReceivedUri: Uri?) {
        super.checkProviderReceivedUri(providerReceivedUri)
        providerReceivedUri?.shouldHaveParameter("screen", "phone")
    }
}
