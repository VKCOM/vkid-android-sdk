package com.vk.id.multibranding.ui.tests

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

public class OneTapScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<OneTapScreen>(semanticsProvider) {

    public val vkButton: KNode = child {
        hasTestTag("oauth_button_vk")
    }
    public val mailButton: KNode = child {
        hasTestTag("oauth_button_mail")
    }
    public val okButton: KNode = child {
        hasTestTag("oauth_button_ok")
    }
}
