package com.vk.id.multibranding.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

public class MultibrandingScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<MultibrandingScreen>(semanticsProvider) {

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
