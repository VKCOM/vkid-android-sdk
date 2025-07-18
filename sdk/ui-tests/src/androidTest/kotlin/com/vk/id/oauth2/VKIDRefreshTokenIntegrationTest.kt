@file:OptIn(InternalVKIDApi::class)

package com.vk.id.oauth2

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.allure.Owners
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Priority
import com.vk.id.common.allure.Product
import com.vk.id.common.allure.Project
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.common.feature.TestFeature
import com.vk.id.common.mockapi.MockApi
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refresh.VKIDRefreshTokenParams
import com.vk.id.storage.InternalVKIDPreferencesStorage
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
private val REFRESH_TOKEN_NEW_JSON = """{
    |"scopes":["phone","email"],
    |"token":"refresh token new"
|}
""".trimMargin().replace("\n", "")
private val REFRESH_TOKEN_CURRENT_JSON = """{
    |"token":"refresh token current",
    |"scopes":["phone","email"]
|}
""".trimMargin().replace("\n", "")
private const val REFRESH_TOKEN_NEW_VALUE = "refresh token new"
private const val ID_TOKEN_VALUE = "id token"
private const val USER_ID = 123L
private const val STATE = "state"
private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
private const val REFRESH_TOKEN_V1_KEY = "REFRESH_TOKEN_KEY"
private const val REFRESH_TOKEN_V2_KEY = "REFRESH_TOKEN_WITH_SCOPES_KEY"
private const val ID_TOKEN_KEY = "ID_TOKEN_KEY"
private val REFRESH_TOKEN_RESPONSE = InternalVKIDTokenPayloadResponse(
    accessToken = ACCESS_TOKEN_VALUE,
    refreshToken = REFRESH_TOKEN_NEW_VALUE,
    idToken = ID_TOKEN_VALUE,
    expiresIn = 0,
    userId = USER_ID,
    state = STATE,
    scope = "phone email",
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
private val ACCESS_TOKEN = AccessToken(
    token = ACCESS_TOKEN_VALUE,
    idToken = ID_TOKEN_VALUE,
    userID = USER_ID,
    expireTime = -1,
    userData = VKIDUser(
        firstName = MockApi.FIRST_NAME,
        lastName = MockApi.LAST_NAME,
        phone = MockApi.PHONE,
        photo200 = MockApi.AVATAR,
        email = MockApi.EMAIL,
    ),
    scopes = setOf("phone", "email"),
)

@Platform(Platform.ANDROID_AUTO)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.DANIIL_KLIMCHUK)
@Feature(TestFeature.OAUTH_2_1)
@RunWith(AndroidJUnit4::class)
@DisplayName("Рефреш токена")
@Priority(Priority.CRITICAL)
internal class VKIDRefreshTokenIntegrationTest : BaseUiTest() {

    @Test
    @AllureId("2303037")
    @DisplayName("Успешный рефреш токена")
    fun refreshTokenSuccess() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(USER_INFO_RESPONSE))
            .refreshTokenResponse(Result.success(REFRESH_TOKEN_RESPONSE))
            .build()
        every { encryptedStorage.getString(REFRESH_TOKEN_V2_KEY) } returns REFRESH_TOKEN_CURRENT_JSON
        every { encryptedStorage.set(REFRESH_TOKEN_V2_KEY, REFRESH_TOKEN_NEW_JSON) } just runs
        every { encryptedStorage.set(ACCESS_TOKEN_KEY, ACCESS_TOKEN_JSON) } just runs
        every { encryptedStorage.set(ID_TOKEN_KEY, ID_TOKEN_VALUE) } just runs
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Рефрешится токен") {
            runBlocking {
                result = VKID.instance.refreshToken(params = VKIDRefreshTokenParams { state = STATE })
            }
        }
        step("Получен AT") {
            (result as? AccessToken?) shouldBe ACCESS_TOKEN
        }
        step("Не получена ошибка") {
            (result as? VKIDRefreshTokenFail?).shouldBeNull()
        }
    }

    @Test
    @AllureId("2303038")
    @DisplayName("Ошибка отсутствия авторизации при рефреше токена")
    fun notAuthenticatedFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .build()
        every { encryptedStorage.getString(REFRESH_TOKEN_V1_KEY) } returns null
        every { encryptedStorage.getString(REFRESH_TOKEN_V2_KEY) } returns null
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Рефрешится токен") {
            runBlocking {
                result = VKID.instance.refreshToken(params = VKIDRefreshTokenParams { state = STATE })
            }
        }
        step("Не получен AT") {
            (result as? AccessToken?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDRefreshTokenFail?) shouldBe VKIDRefreshTokenFail.NotAuthenticated("You must login before refreshing token")
        }
    }

    @Test
    @AllureId("2303036")
    @DisplayName("Получение ошибки неверного state при рефреше токена")
    fun wrongStateFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .refreshTokenResponse(Result.success(REFRESH_TOKEN_RESPONSE.copy(state = "wrong state")))
            .build()
        every { encryptedStorage.getString(REFRESH_TOKEN_V2_KEY) } returns REFRESH_TOKEN_CURRENT_JSON
        every { deviceIdStorage.getDeviceId() } returns "device id"
        every { prefsStore.clear() } just runs
        var result: Any? = null
        step("Рефрешится токен") {
            runBlocking {
                result = VKID.instance.refreshToken(params = VKIDRefreshTokenParams { state = STATE })
            }
        }
        step("Не получен AT") {
            (result as? AccessToken?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDRefreshTokenFail?) shouldBe VKIDRefreshTokenFail.FailedOAuthState("Wrong state for getting user info")
        }
    }

    @Test
    @AllureId("2303039")
    @DisplayName("Получение ошибки от апи при рефреше токена")
    fun refreshTokenApiFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .refreshTokenResponse(Result.success(InternalVKIDTokenPayloadResponse(error = "some error")))
            .build()
        every { encryptedStorage.getString(REFRESH_TOKEN_V2_KEY) } returns REFRESH_TOKEN_CURRENT_JSON
        every { deviceIdStorage.getDeviceId() } returns "device id"
        every { prefsStore.clear() } just runs
        var result: Any? = null
        step("Рефрешится токен") {
            runBlocking {
                result = VKID.instance.refreshToken(params = VKIDRefreshTokenParams { state = STATE })
            }
        }
        step("Не получен AT") {
            (result as? AccessToken?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDRefreshTokenFail?).shouldBeInstanceOf<VKIDRefreshTokenFail.FailedApiCall>()
        }
    }

    @Test
    @AllureId("2303035")
    @DisplayName("Получение ошибки получения пользовательских данных при рефреше токена")
    fun userDataApiFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(InternalVKIDUserInfoPayloadResponse(error = "some error")))
            .refreshTokenResponse(Result.success(REFRESH_TOKEN_RESPONSE))
            .build()
        every { encryptedStorage.getString(REFRESH_TOKEN_V2_KEY) } returns REFRESH_TOKEN_CURRENT_JSON
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Рефрешится токен") {
            runBlocking {
                result = VKID.instance.refreshToken(params = VKIDRefreshTokenParams { state = STATE })
            }
        }
        step("Не получен AT") {
            (result as? AccessToken?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDRefreshTokenFail?).shouldBeInstanceOf<VKIDRefreshTokenFail.FailedApiCall>()
        }
    }

    private suspend fun VKID.refreshToken(
        params: VKIDRefreshTokenParams = VKIDRefreshTokenParams { }
    ): Any = suspendCoroutine {
        runBlocking {
            refreshToken(
                callback = object : VKIDRefreshTokenCallback {
                    override fun onSuccess(token: AccessToken) = it.resume(token)
                    override fun onFail(fail: VKIDRefreshTokenFail) = it.resume(fail)
                },
                params = params
            )
        }
    }
}
