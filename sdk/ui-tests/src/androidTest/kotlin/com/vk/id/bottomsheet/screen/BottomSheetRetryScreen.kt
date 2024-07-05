package com.vk.id.bottomsheet.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class BottomSheetRetryScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<BottomSheetRetryScreen>(semanticsProvider) {

    public val retryButton: KNode = child {
        hasTestTag("vkid_retry_btn")
    }
}
