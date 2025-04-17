@file:OptIn(InternalVKIDApi::class)

package com.vk.id.oauth2

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.allure.Owners
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Priority
import com.vk.id.common.allure.Product
import com.vk.id.common.allure.Project
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.common.feature.TestFeature
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.logout.VKIDLogoutCallback
import com.vk.id.logout.VKIDLogoutFail
import com.vk.id.storage.InternalVKIDPreferencesStorage
import com.vk.id.test.InternalVKIDLogoutPayloadResponse
import com.vk.id.test.InternalVKIDTestBuilder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.Owner
import io.qameta.allure.kotlin.junit4.DisplayName
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val ACCESS_TOKEN_JSON = """{
    |"expireTime":-1,
    |"idToken":"id token",
    |"scopes":["phone","email"],
    |"token":"access token",
    |"userData":{
        |"email":"email",
        |"firstName":"first name",
        |"lastName":"last name",
        |"phone":"phone",
        |"photo200":"avatar"
    |},
    |"userID":123
|}
""".trimMargin().replace("\n", "")
private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
private const val REFRESH_TOKEN_V1_KEY = "REFRESH_TOKEN_KEY"
private const val REFRESH_TOKEN_V2_KEY = "REFRESH_TOKEN_WITH_SCOPES_KEY"
private const val ID_TOKEN_KEY = "ID_TOKEN_KEY"

@Platform(Platform.ANDROID_AUTO)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.DANIIL_KLIMCHUK)
@Feature(TestFeature.OAUTH_2_1)
@RunWith(AndroidJUnit4::class)
@DisplayName("Логаут")
@Priority(Priority.CRITICAL)
internal class VKIDLogoutIntegrationTest : BaseUiTest() {

    @Test
    @AllureId("2303051")
    @DisplayName("Успешный логаут")
    fun logoutSuccess() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .logoutResponse(Result.success(InternalVKIDLogoutPayloadResponse()))
            .build()
        every { encryptedStorage.getString(ACCESS_TOKEN_KEY) } returns ACCESS_TOKEN_JSON
        every { encryptedStorage.set(REFRESH_TOKEN_V1_KEY, null) } just runs
        every { encryptedStorage.set(REFRESH_TOKEN_V2_KEY, null) } just runs
        every { encryptedStorage.set(ACCESS_TOKEN_KEY, null) } just runs
        every { encryptedStorage.set(ID_TOKEN_KEY, null) } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Получается пользователь") {
            runBlocking {
                result = VKID.instance.logout()
            }
        }
        step("Логаут завершается успехом") {
            (result as? Unit?) shouldBe Unit
        }
        step("Не получена ошибка") {
            (result as? VKIDLogoutFail?).shouldBeNull()
        }
    }

    @Test
    @AllureId("2303054")
    @DisplayName("Ошибка логаута из-за отсутствия авторизации")
    fun notAuthenticatedFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .build()
        every { encryptedStorage.getString(ACCESS_TOKEN_KEY) } returns null
        every { encryptedStorage.set(REFRESH_TOKEN_V1_KEY, null) } just runs
        every { encryptedStorage.set(REFRESH_TOKEN_V2_KEY, null) } just runs
        every { encryptedStorage.set(ACCESS_TOKEN_KEY, null) } just runs
        every { encryptedStorage.set(ID_TOKEN_KEY, null) } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Получается пользователь") {
            runBlocking {
                result = VKID.instance.logout()
            }
        }
        step("Нет вызова колбека об успехе") {
            (result as? Unit?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDLogoutFail?) shouldBe VKIDLogoutFail.NotAuthenticated("Not authorized, can't logout")
        }
    }

    @Test
    @AllureId("2303053")
    @DisplayName("Ошибка при логауте из-за ошибки апи запроса")
    fun apiCalFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .logoutResponse(Result.success(InternalVKIDLogoutPayloadResponse(error = "some error")))
            .build()
        every { encryptedStorage.getString(ACCESS_TOKEN_KEY) } returns ACCESS_TOKEN_JSON
        every { encryptedStorage.set(REFRESH_TOKEN_V1_KEY, null) } just runs
        every { encryptedStorage.set(REFRESH_TOKEN_V2_KEY, null) } just runs
        every { encryptedStorage.set(ACCESS_TOKEN_KEY, null) } just runs
        every { encryptedStorage.set(ID_TOKEN_KEY, null) } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Получается пользователь") {
            runBlocking {
                result = VKID.instance.logout()
            }
        }
        step("Нет вызова колбека об успехе") {
            (result as? Unit?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDLogoutFail?).shouldBeInstanceOf<VKIDLogoutFail.FailedApiCall>()
        }
    }

    @Test
    @AllureId("2303052")
    @DisplayName("Успешный логаут из-за просроченного токена")
    fun logoutSuccessAfterInvalidToken() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .logoutResponse(Result.success(InternalVKIDLogoutPayloadResponse(error = "invalid_token")))
            .build()
        every { encryptedStorage.getString(ACCESS_TOKEN_KEY) } returns ACCESS_TOKEN_JSON
        every { encryptedStorage.set(REFRESH_TOKEN_V1_KEY, null) } just runs
        every { encryptedStorage.set(REFRESH_TOKEN_V2_KEY, null) } just runs
        every { encryptedStorage.set(ACCESS_TOKEN_KEY, null) } just runs
        every { encryptedStorage.set(ID_TOKEN_KEY, null) } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Получается пользователь") {
            runBlocking {
                result = VKID.instance.logout()
            }
        }
        step("Логаут завершается успехом") {
            (result as? Unit?) shouldBe Unit
        }
        step("Не получена ошибка") {
            (result as? VKIDLogoutFail?).shouldBeNull()
        }
    }

    private suspend fun VKID.logout(): Any = suspendCoroutine {
        runBlocking {
            logout(
                callback = object : VKIDLogoutCallback {
                    override fun onSuccess() = it.resume(Unit)
                    override fun onFail(fail: VKIDLogoutFail) = it.resume(fail)
                },
            )
        }
    }
}
