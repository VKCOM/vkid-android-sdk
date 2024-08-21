@file:OptIn(InternalVKIDApi::class)

package com.vk.id.provider

import android.content.pm.PackageManager
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
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

    @Before
    public fun initIntents() {
        Intents.init()
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

    private fun vkidBuilder(): InternalVKIDTestBuilder = InternalVKIDTestBuilder(composeTestRule.activity)
}

private fun AutoTestActivity.mockThereIsNoProviderNoBrowser() {
    val pm = mockk<PackageManager>()
    every {
        pm.queryIntentServices(
            match {
                it.action == "com.vk.silentauth.action.GET_INFO"
            },
            eq(0)
        )
    } returns emptyList()
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

private fun AutoTestActivity.releaseMockPM() {
    mockPackageManager = null
}
