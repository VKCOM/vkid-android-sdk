@file:OptIn(InternalVKIDApi::class)

package com.vk.id.provider

import android.app.Activity
import android.app.Instrumentation
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.pm.Signature
import android.net.Uri
import android.os.Build
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.activity.AutoTestActivity
import com.vk.id.common.activity.AutoTestActivityRule
import com.vk.id.common.allure.Owners
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Priority
import com.vk.id.common.allure.Product
import com.vk.id.common.allure.Project
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.test.InternalVKIDTestBuilder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.qameta.allure.kotlin.Owner
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Platform(Platform.ANDROID_AUTO)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.SERGEY_GOLOVIN)
@Priority(Priority.CRITICAL)
public class OutgoingIntentsTest : BaseUiTest() {

    @get:Rule
    public val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

    private lateinit var serviceCredentials: ServiceCredentials

    @Before
    public fun initIntents() {
        Intents.init()
        serviceCredentials = readCreds(composeTestRule.activity)
    }

    @After
    public fun releaseIntents() {
        Intents.release()
    }

    @Test
    public fun noProviderNoBrowserAvailable() {
        var receivedFail: VKIDAuthFail? = null
        var receivedToken: AccessToken? = null
        before {
            vkidBuilder().build()
            composeTestRule.activity.mockThereIsNoProviderNoBrowser()
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
            composeTestRule.activity.releaseMockPM()
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
    public fun noProviderOnlyBrowserAvailable() {
        before {
            vkidBuilder().build()
            composeTestRule.activity.mockThereIsOnlyBrowser()
            // prevent actually starting browser
            Intents.intending(
                allOf(
                    IntentMatchers.hasAction(Intent.ACTION_VIEW),
                    IntentMatchers.hasPackage(MockChrome.PACKAGE_NAME),
                    IntentMatchers.hasData(
                        matchBrowserIntent(
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
            composeTestRule.activity.releaseMockPM()
        }.run {
            step("Отправлен корректный intent в браузер") {
                flakySafely {
                    Intents.intended(
                        allOf(
                            IntentMatchers.hasAction(Intent.ACTION_VIEW),
                            IntentMatchers.hasPackage(MockChrome.PACKAGE_NAME),
                            IntentMatchers.hasData(
                                matchBrowserIntent(
                                    "https://id.vk.com/authorize",
                                    clientId = serviceCredentials.clientID,
                                    responseType = "code",
                                    redirectUri = serviceCredentials.redirectUri
                                )
                            )
                        )
                    )
                }
            }
        }
    }

    private fun vkidBuilder(): InternalVKIDTestBuilder = InternalVKIDTestBuilder(composeTestRule.activity)
}

private fun matchBrowserIntent(uriPrefix: String, clientId: String? = null, responseType: String? = null, redirectUri: String? = null): Matcher<Uri> {
    return object : TypeSafeMatcher<Uri>() {
        override fun describeTo(description: Description) {
            description.appendText(
                "Uri should start with $uriPrefix and have params client_id=$clientId, response_type=$responseType, redirect_uri=$redirectUri"
            )
        }

        override fun matchesSafely(item: Uri): Boolean {
            val prefixCorrect = item.toString().startsWith(uriPrefix)
            val clientIdCorrect = clientId == null || item.getQueryParameter("client_id") == clientId
            val responseTypeCorrect = responseType == null || item.getQueryParameter("response_type") == responseType
            val redirectUriCorrect = redirectUri == null || item.getQueryParameter("redirect_uri")?.startsWith(redirectUri) ?: false
            return prefixCorrect && clientIdCorrect && responseTypeCorrect && redirectUriCorrect
        }
    }
}

private fun AutoTestActivity.mockThereIsNoProviderNoBrowser() {
    val pm = mockk<PackageManager>()
    pm.mockNoProviders()
    every {
        pm.resolveActivity(
            match {
                it.data.toString() == "http://www.example.com"
            },
            eq(0)
        )
    } returns null
    every {
        pm.queryIntentActivities(
            match {
                it.data.toString() == "http://www.example.com"
            },
            any<Int>()
        )
    } returns emptyList()

    this.mockPackageManager = pm
}

private fun AutoTestActivity.mockThereIsOnlyBrowser() {
    val pm = mockk<PackageManager>()
    pm.mockNoProviders()
    every {
        pm.resolveActivity(
            match {
                it.data.toString() == "http://www.example.com"
            },
            eq(0)
        )
    } returns null
    every {
        pm.queryIntentActivities(
            match {
                it.data.toString() == "http://www.example.com"
            },
            any<Int>()
        )
    } returns listOf(
        ResolveInfo().apply {
            filter = IntentFilter(Intent.ACTION_VIEW)
            filter.addCategory(Intent.CATEGORY_BROWSABLE)
            filter.addDataScheme("http")
            filter.addDataScheme("https")
            activityInfo = ActivityInfo().apply {
                this.packageName = MockChrome.PACKAGE_NAME
            }
        }
    )
    every {
        pm.getPackageInfo(eq("com.android.chrome"), any<Int>())
    } returns PackageInfo().apply {
        this.packageName = MockChrome.PACKAGE_NAME
        this.versionName = MockChrome.VERSION
        this.signatures = arrayOf(Signature(MockChrome.SIGNATURE))
    }

    // for BrowserSelector::hasWarmupService
    every {
        pm.resolveService(
            match {
                it.action == "android.support.customtabs.action.CustomTabsService" &&
                    it.`package` == MockChrome.PACKAGE_NAME
            },
            any<Int>()
        )
    } returns ResolveInfo()

    this.mockPackageManager = pm
}

private fun PackageManager.mockNoProviders() {
    every {
        queryIntentServices(
            match {
                it.action == "com.vk.silentauth.action.GET_INFO"
            },
            eq(0)
        )
    } returns emptyList()
}

private fun AutoTestActivity.releaseMockPM() {
    mockPackageManager = null
}

private data class ServiceCredentials(
    val clientID: String,
    val clientSecret: String,
    val redirectUri: String
)

private fun readCreds(context: Context): ServiceCredentials {
    val componentName = ComponentName(context, "com.vk.id.internal.auth.AuthActivity")
    val flags = PackageManager.GET_META_DATA or PackageManager.GET_ACTIVITIES
    val ai: ActivityInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.packageManager.getActivityInfo(
            componentName,
            PackageManager.ComponentInfoFlags.of(flags.toLong())
        )
    } else {
        context.packageManager.getActivityInfo(
            componentName,
            flags
        )
    }
    val clientID = ai.metaData.getInt("VKIDClientID").toString()
    val clientSecret = ai.metaData.getString("VKIDClientSecret")!!
    val redirectScheme = ai.metaData.getString("VKIDRedirectScheme")
    val redirectHost = ai.metaData.getString("VKIDRedirectHost")
    val redirectUri = "$redirectScheme://$redirectHost/blank.html"

    return ServiceCredentials(clientID, clientSecret, redirectUri)
}
