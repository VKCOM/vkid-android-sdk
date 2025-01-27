package com.vk.id.groupsubscription.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

public class GroupSubscriptionLoadedScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<GroupSubscriptionLoadedScreen>(semanticsProvider) {

    public val sheet: KNode = child {
        hasTestTag("group_subscription_sheet")
    }

    public val closeButton: KNode = child {
        hasTestTag("group_subscription_close")
    }

    public val subscribeButton: KNode = child {
        hasTestTag("group_subscription_subscribe")
    }

    public val laterButton: KNode = child {
        hasTestTag("group_subscription_later")
    }
}
