package com.vk.id.onetap.compose

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

internal class OneTapScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<OneTapScreen>(semanticsProvider) {

    val oneTapButton: KNode = child {
        hasTestTag("vkid_button")
    }
}