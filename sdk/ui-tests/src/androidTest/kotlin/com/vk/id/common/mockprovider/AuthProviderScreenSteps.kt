package com.vk.id.common.mockprovider

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.kaspersky.kaspresso.testcases.api.scenario.BaseScenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.compose.node.element.ComposeScreen

public class ContinueAuthScenario(
    semanticsProvider: SemanticsNodeInteractionsProvider,
) : BaseScenario<Unit>() {
    override val steps: TestContext<Unit>.() -> Unit = {
        step("Complete provider auth") {
            ComposeScreen.onComposeScreen<AuthProviderScreen>(semanticsProvider) {
                continueButton {
                    performClick()
                }
            }
        }
    }
}
