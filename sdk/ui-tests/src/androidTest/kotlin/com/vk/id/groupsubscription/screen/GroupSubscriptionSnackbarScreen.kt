package com.vk.id.groupsubscription.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

public class GroupSubscriptionSnackbarScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<GroupSubscriptionSnackbarScreen>(semanticsProvider) {

    public val snackbar: KNode = child {
        hasTestTag("group_subscription_snackbar")
    }
}
