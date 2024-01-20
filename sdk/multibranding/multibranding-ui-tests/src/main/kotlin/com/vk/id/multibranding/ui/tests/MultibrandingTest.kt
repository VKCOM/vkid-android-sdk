package com.vk.id.multibranding.ui.tests

import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.OAuth
import com.vk.id.common.baseauthtest.BaseAuthTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
public abstract class MultibrandingTest(
    private val oAuth: OAuth,
    skipTest: Boolean = false,
) : BaseAuthTest(
    oAuth = oAuth,
    skipTest = skipTest
) {

    private companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data() = OAuth.entries
    }

    protected override fun TestContext<Unit>.startAuth(): Unit = step("Start auth") {
        ComposeScreen.onComposeScreen<OneTapScreen>(composeTestRule) {
            when (oAuth) {
                OAuth.VK -> vkButton.performClick()
                OAuth.MAIL -> mailButton.performClick()
                OAuth.OK -> okButton.performClick()
            }
        }
    }
}
