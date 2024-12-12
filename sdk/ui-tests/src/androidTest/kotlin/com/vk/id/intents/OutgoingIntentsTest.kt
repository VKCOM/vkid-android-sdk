@file:OptIn(InternalVKIDApi::class)

package com.vk.id.intents

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.activity.AutoTestActivityRule
import com.vk.id.common.allure.Owners
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Priority
import com.vk.id.common.allure.Product
import com.vk.id.common.allure.Project
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.common.feature.TestFeature
import com.vk.id.common.mockprovider.pm.MockChrome
import com.vk.id.common.mockprovider.pm.MockPmNoProvidersNoBrowsers
import com.vk.id.common.mockprovider.pm.MockPmOnlyBrowser
import com.vk.id.common.mockprovider.pm.MockPmVKProvider
import com.vk.id.common.mockprovider.pm.MockVK
import com.vk.id.internal.context.InternalVKIDPackageManager
import com.vk.id.test.InternalVKIDTestBuilder
import com.vk.id.util.ServiceCredentials
import com.vk.id.util.readVKIDCredentials
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.Owner
import io.qameta.allure.kotlin.junit4.DisplayName
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

@Platform(Platform.ANDROID_AUTO)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.SERGEY_GOLOVIN)
@Feature(TestFeature.OAUTH_2_1)
@Priority(Priority.CRITICAL)
@DisplayName("Интерфейс авторизации")
public class OutgoingIntentsTest : BaseUiTest() {

    @get:Rule
    public val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

    private var serviceCredentials: ServiceCredentials? = null

    @Before
    public fun readCreds() {
        serviceCredentials = readVKIDCredentials(composeTestRule.activity)
    }

    @Test
    @AllureId("2361814")
    @DisplayName("Авторизация без провайдеров и установленных браузеров")
    public fun noProviderNoBrowserAvailable() {
        var receivedFail: VKIDAuthFail? = null
        var receivedToken: AccessToken? = null
        before {
            vkidBuilder(MockPmNoProvidersNoBrowsers()).build()
            VKID.instance.authorize(
                composeTestRule.activity,
                callback = object : VKIDAuthCallback {
                    override fun onAuth(accessToken: AccessToken) {
                        receivedToken = accessToken
                    }
                    override fun onFail(fail: VKIDAuthFail) {
                        receivedFail = fail
                    }
                }
            )
        }.after {
        }.run {
            step("Получена ошибка NoBrowserAvailable") {
                flakySafely {
                    receivedToken.shouldBeNull()
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.NoBrowserAvailable>()
                }
            }
        }
    }

    @Test
    @AllureId("2361815")
    @DisplayName("Авторизация без провайдеров, но с установленным браузером")
    public fun noProviderOnlyBrowserAvailable() {
        before {
            vkidBuilder(MockPmOnlyBrowser()).build()
            // prevent actually starting browser
            Intents.intending(
                allOf(
                    IntentMatchers.hasAction(Intent.ACTION_VIEW),
                    IntentMatchers.hasPackage(MockChrome.PACKAGE_NAME),
                    IntentMatchers.hasData(
                        matchIntentUri(
                            uriPrefix = "https://id.vk.com/authorize",
                        )
                    )
                )
            ).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))
            VKID.instance.authorize(
                composeTestRule.activity,
                mockk(relaxed = true)
            )
        }.after {
        }.run {
            step("Отправлен корректный intent в браузер") {
                flakySafely {
                    Intents.intended(
                        allOf(
                            IntentMatchers.hasAction(Intent.ACTION_VIEW),
                            IntentMatchers.hasPackage(MockChrome.PACKAGE_NAME),
                            IntentMatchers.hasData(
                                matchIntentUri(
                                    "https://id.vk.com/authorize",
                                    clientId = serviceCredentials?.clientID,
                                    responseType = "code",
                                    redirectUri = serviceCredentials?.redirectUri
                                )
                            )
                        )
                    )
                }
            }
        }
    }

    @Test
    @AllureId("2361816")
    @DisplayName("Авторизация с установленным VK провайдером")
    public fun providerAvailableVK() {
        before {
            vkidBuilder(MockPmVKProvider()).build()
            // prevent actually starting provider
            Intents.intending(
                allOf(
                    IntentMatchers.hasAction(Intent.ACTION_VIEW),
                    IntentMatchers.hasData(
                        matchIntentUri(
                            uriPrefix = MockVK.APP_URI,
                        )
                    )
                )
            ).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))
            VKID.instance.authorize(
                composeTestRule.activity,
                mockk(relaxed = true)
            )
        }.after {
        }.run {
            step("Отправлен корректный intent в провайдер") {
                flakySafely {
                    // com.vkontakte.android://vkcexternalauth-codeflow?app_id=51925238&response_type=code&redirect_uri=vk51925238%3A%2F%2Fvk.com%2Fblank.html%3Foauth2_params%3DeyJzY29wZSI6IiIsInN0YXRzX2luZm8iOnsiZmxvd19zb3VyY2UiOiJmcm9tX2N1c3RvbV9hdXRoIiwic2Vzc2lvbl9pZCI6IjMyMTVlYjNjLWVmMmQtNDFjNS1iMTg1LTM2YjcwYzViMGI4ZCJ9fQ%253D%253D&code_challenge_method=sha256&code_challenge=BceBr0GI_PHEjOiw9R15-ziH-ypPn-s6cNk-wx41tMY&state=Il2HH1UmwMGWut5WXuAYKuMMPz5p53WH&uuid=Il2HH1UmwMGWut5WXuAYKuMMPz5p53WH&lang_id=3&scheme=space_gray
                    Intents.intended(
                        allOf(
                            IntentMatchers.hasAction(Intent.ACTION_VIEW),
                            IntentMatchers.hasData(
                                matchIntentUri(
                                    MockVK.APP_URI,
                                    appId = serviceCredentials?.clientID,
                                    responseType = "code",
                                    redirectUri = serviceCredentials?.redirectUri
                                )
                            )
                        )
                    )
                }
            }
        }
    }

    private fun vkidBuilder(pm: InternalVKIDPackageManager): InternalVKIDTestBuilder =
        InternalVKIDTestBuilder(composeTestRule.activity).overridePackageManager(pm)

    public companion object {
        @JvmStatic
        @BeforeClass
        public fun initIntents() {
            Intents.init()
        }

        @JvmStatic
        @AfterClass
        public fun releaseIntents() {
            Intents.release()
        }
    }
}

private fun matchIntentUri(
    uriPrefix: String,
    clientId: String? = null,
    appId: String? = null,
    responseType: String? = null,
    redirectUri: String? = null
): Matcher<Uri> {
    return object : TypeSafeMatcher<Uri>() {
        override fun describeTo(description: Description) {
            description.appendText(
                "Uri should start with $uriPrefix and have params client_id=$clientId, " +
                    "response_type=$responseType, redirect_uri=$redirectUri, app_id = $appId"
            )
        }

        override fun matchesSafely(item: Uri): Boolean {
            val prefixCorrect = item.toString().startsWith(uriPrefix)
            val clientIdCorrect = clientId == null || item.getQueryParameter("client_id") == clientId
            val appIdCorrect = appId == null || item.getQueryParameter("app_id") == appId
            val responseTypeCorrect = responseType == null || item.getQueryParameter("response_type") == responseType
            val redirectUriCorrect = redirectUri == null || item.getQueryParameter("redirect_uri")?.startsWith(redirectUri) ?: false
            return prefixCorrect && clientIdCorrect && responseTypeCorrect && redirectUriCorrect && appIdCorrect
        }
    }
}
