package com.vk.id.multibranding.ui.tests

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

public class AuthProviderScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<AuthProviderScreen>(semanticsProvider) {

    public val continueButton: KNode = child {
        hasTestTag("mock_auth_continue")
    }
}
