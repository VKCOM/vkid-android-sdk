package com.vk.id.bottomsheet.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.vk.id.common.activity.AutoTestActivityRule
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

internal class BottomSheetRetryScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<BottomSheetRetryScreen>(semanticsProvider) {
    val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

    val retryButton: KNode = child {
        hasTestTag("vkid_retry_btn")
    }
}
