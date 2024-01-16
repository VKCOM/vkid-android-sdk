package com.vk.id.common.mockprovider

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.compose.node.element.ComposeScreen

public fun TestContext<Unit>.continueAuth(
    semanticsProvider: SemanticsNodeInteractionsProvider,
): Unit = step("Complete provider auth") {
    ComposeScreen.onComposeScreen<AuthProviderScreen>(semanticsProvider) {
        continueButton {
            performClick()
        }
    }
}
