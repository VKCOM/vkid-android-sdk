@file:OptIn(InternalVKIDApi::class)

package com.vk.id.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.allure.Owners
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Product
import com.vk.id.common.allure.Project
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.common.feature.TestFeature
import com.vk.id.common.mockapi.MockApi
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import com.vk.id.refreshuser.VKIDGetUserParams
import com.vk.id.storage.InternalVKIDEncryptedSharedPreferencesStorage
import com.vk.id.test.InternalVKIDTestBuilder
import com.vk.id.test.InternalVKIDTokenPayloadResponse
import com.vk.id.test.InternalVKIDUserInfoPayloadResponse
import com.vk.id.test.InternalVKIDUserPayloadResponse
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

private const val ACCESS_TOKEN_VALUE = "access token"
private val ACCESS_TOKEN_JSON = """{
    |"expireTime":-1,
    |"idToken":"id token",
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
private const val REFRESH_TOKEN_CURRENT_VALUE = "refresh token current"
private const val REFRESH_TOKEN_NEW_VALUE = "refresh token new"
private const val ID_TOKEN_VALUE = "id token"
private const val USER_ID = 123L
private const val STATE = "state"
private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
private const val ID_TOKEN_KEY = "ID_TOKEN_KEY"
private val REFRESH_TOKEN_RESPONSE = InternalVKIDTokenPayloadResponse(
    accessToken = ACCESS_TOKEN_VALUE,
    refreshToken = REFRESH_TOKEN_NEW_VALUE,
    idToken = ID_TOKEN_VALUE,
    expiresIn = 0,
    userId = USER_ID,
    state = STATE,
)
private val USER_INFO_RESPONSE = InternalVKIDUserInfoPayloadResponse(
    user = InternalVKIDUserPayloadResponse(
        firstName = MockApi.FIRST_NAME,
        lastName = MockApi.LAST_NAME,
        email = MockApi.EMAIL,
        phone = MockApi.PHONE,
        avatar = MockApi.AVATAR,
    ),
    state = MockApi.STATE
)
private val USER = VKIDUser(
    firstName = MockApi.FIRST_NAME,
    lastName = MockApi.LAST_NAME,
    phone = MockApi.PHONE,
    photo200 = MockApi.AVATAR,
    email = MockApi.EMAIL,
)

@Platform(Platform.ANDROID_AUTO)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.DANIIL_KLIMCHUK)
@Feature(TestFeature.OAUTH_2_1)
@RunWith(AndroidJUnit4::class)
@DisplayName("Получение пользовательских данных")
internal class VKIDGetUserDataIntegrationTest : BaseUiTest() {

    @Test
    @AllureId("2303048")
    @DisplayName("Успешное получение пользовательских данных")
    fun getUserSuccess() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDEncryptedSharedPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(USER_INFO_RESPONSE))
            .build()
        every { encryptedStorage.getString(ACCESS_TOKEN_KEY) } returns ACCESS_TOKEN_JSON
        every { encryptedStorage.set(REFRESH_TOKEN_KEY, REFRESH_TOKEN_NEW_VALUE) } just runs
        every { encryptedStorage.set(ACCESS_TOKEN_KEY, ACCESS_TOKEN_JSON) } just runs
        every { encryptedStorage.set(ID_TOKEN_KEY, ID_TOKEN_VALUE) } just runs
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Получается пользователь") {
            runBlocking {
                result = VKID.instance.getUserData(params = VKIDGetUserParams { refreshTokenState = STATE })
            }
        }
        step("Получен AT") {
            (result as? VKIDUser?) shouldBe USER
        }
        step("Не получена ошибка") {
            (result as? VKIDGetUserFail?).shouldBeNull()
        }
    }

    @Test
    @AllureId("2303046")
    @DisplayName("Успешное получение пользовательских данных после рефреша токена")
    fun getUserWithRefreshSuccess() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDEncryptedSharedPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .getUserInfoResponses(
                Result.success(InternalVKIDUserInfoPayloadResponse(error = "invalid_token")),
                Result.success(USER_INFO_RESPONSE),
            )
            .refreshTokenResponse(Result.success(REFRESH_TOKEN_RESPONSE))
            .build()
        every { encryptedStorage.getString(ACCESS_TOKEN_KEY) } returns ACCESS_TOKEN_JSON
        every { encryptedStorage.getString(REFRESH_TOKEN_KEY) } returns REFRESH_TOKEN_CURRENT_VALUE
        every { encryptedStorage.set(REFRESH_TOKEN_KEY, REFRESH_TOKEN_NEW_VALUE) } just runs
        every { encryptedStorage.set(ACCESS_TOKEN_KEY, ACCESS_TOKEN_JSON) } just runs
        every { encryptedStorage.set(ID_TOKEN_KEY, ID_TOKEN_VALUE) } just runs
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Получается пользователь") {
            runBlocking {
                result = VKID.instance.getUserData(params = VKIDGetUserParams { refreshTokenState = STATE })
            }
        }
        step("Получен пользователь") {
            (result as? VKIDUser?) shouldBe USER
        }
        step("Не получена ошибка") {
            (result as? VKIDGetUserFail?).shouldBeNull()
        }
    }

    @Test
    @AllureId("2303049")
    @DisplayName("Ошибка получения пользовательских данных из-за ошибки рефреша токена")
    fun getUserWithRefreshFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDEncryptedSharedPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(InternalVKIDUserInfoPayloadResponse(error = "invalid_token")),)
            .refreshTokenResponse(Result.success(REFRESH_TOKEN_RESPONSE.copy(error = "some error")))
            .build()
        every { encryptedStorage.getString(ACCESS_TOKEN_KEY) } returns ACCESS_TOKEN_JSON
        every { encryptedStorage.getString(REFRESH_TOKEN_KEY) } returns REFRESH_TOKEN_CURRENT_VALUE
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Получается пользователь") {
            runBlocking {
                result = VKID.instance.getUserData(params = VKIDGetUserParams { refreshTokenState = STATE })
            }
        }
        step("Получен пользователь") {
            (result as? VKIDUser?).shouldBeNull()
        }
        step("Не получена ошибка") {
            (result as? VKIDGetUserFail?).shouldBeInstanceOf<VKIDGetUserFail.FailedApiCall>()
        }
    }

    @Test
    @AllureId("2303050")
    @DisplayName("Ошибка получения пользовательских данных из-за ошибки апи")
    fun apiCallFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDEncryptedSharedPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(InternalVKIDUserInfoPayloadResponse(error = "some error")),)
            .build()
        every { encryptedStorage.getString(ACCESS_TOKEN_KEY) } returns ACCESS_TOKEN_JSON
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Получается пользователь") {
            runBlocking {
                result = VKID.instance.getUserData(params = VKIDGetUserParams { refreshTokenState = STATE })
            }
        }
        step("Получен пользователь") {
            (result as? VKIDUser?).shouldBeNull()
        }
        step("Не получена ошибка") {
            (result as? VKIDGetUserFail?).shouldBeInstanceOf<VKIDGetUserFail.FailedApiCall>()
        }
    }

    @Test
    @AllureId("2303047")
    @DisplayName("Ошибка получения пользовательских данных из-за отсутствия авторизации")
    fun notAuthorizedFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDEncryptedSharedPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .build()
        every { encryptedStorage.getString(ACCESS_TOKEN_KEY) } returns null
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Получается пользователь") {
            runBlocking {
                result = VKID.instance.getUserData(params = VKIDGetUserParams { refreshTokenState = STATE })
            }
        }
        step("Получен пользователь") {
            (result as? VKIDUser?).shouldBeNull()
        }
        step("Не получена ошибка") {
            (result as? VKIDGetUserFail?) shouldBe VKIDGetUserFail.NotAuthenticated("Not authorized")
        }
    }

    private suspend fun VKID.getUserData(
        params: VKIDGetUserParams = VKIDGetUserParams {}
    ): Any = suspendCoroutine {
        runBlocking {
            getUserData(
                callback = object : VKIDGetUserCallback {
                    override fun onSuccess(user: VKIDUser) = it.resume(user)
                    override fun onFail(fail: VKIDGetUserFail) = it.resume(fail)
                },
                params = params
            )
        }
    }
}
