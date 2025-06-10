package com.vk.id.groupsubscription.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

public class GroupSubscriptionErrorScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<GroupSubscriptionErrorScreen>(semanticsProvider) {

    public val sheet: KNode = child {
        hasTestTag("group_subscription_sheet")
    }

    public val retryButton: KNode = child {
        hasTestTag("group_subscription_retry")
    }

    public val cancelButton: KNode = child {
        hasTestTag("group_subscription_cancel")
    }
}
