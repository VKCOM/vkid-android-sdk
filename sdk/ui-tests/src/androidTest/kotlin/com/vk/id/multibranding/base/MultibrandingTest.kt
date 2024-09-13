package com.vk.id.multibranding.base

import android.net.Uri
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.OAuth
import com.vk.id.common.baseauthtest.BaseAuthTest
import com.vk.id.util.shouldHaveParameter
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

    protected override fun TestContext<Unit>.startAuth(): Unit = step("Начало авторизации") {
        ComposeScreen.onComposeScreen<com.vk.id.multibranding.screen.MultibrandingScreen>(composeTestRule) {
            when (oAuth) {
                OAuth.VK -> vkButton.performClick()
                OAuth.MAIL -> mailButton.performClick()
                OAuth.OK -> okButton.performClick()
            }
        }
    }

    protected override val supportedUriParams: Set<String>
        get() = super.supportedUriParams.toMutableSet().apply {
            add("action")
            add("provider")
        }

    protected override fun checkProviderReceivedUri(providerReceivedUri: Uri?) {
        super.checkProviderReceivedUri(providerReceivedUri)
        providerReceivedUri?.shouldHaveParameter(
            "provider",
            when (oAuth) {
                OAuth.VK -> "vkid"
                OAuth.MAIL -> "mail_ru"
                OAuth.OK -> "ok_ru"
            }
        )
    }
}
