package com.vk.id.onetap

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

public class OneTapScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<OneTapScreen>(semanticsProvider) {

    public val oneTapButton: KNode = child {
        hasTestTag("vkid_button")
    }

    public val signInToAnotherAccountButton: KNode = child {
        hasTestTag("sign_in_to_another_account")
    }
}
