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
import com.vk.id.exchangetoken.VKIDExchangeTokenCallback
import com.vk.id.exchangetoken.VKIDExchangeTokenFail
import com.vk.id.exchangetoken.VKIDExchangeTokenParams
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.storage.InternalVKIDPreferencesStorage
import com.vk.id.test.InternalVKIDCodePayloadResponse
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

private const val V1_TOKEN = "v1 token"
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
private const val REFRESH_TOKEN_NEW_VALUE = "refresh token new"
private const val ID_TOKEN_VALUE = "id token"
private const val USER_ID = 123L
private const val STATE = "state"
private const val NEW_DEVICE_ID = "new device id"
private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_WITH_SCOPES_KEY"
private const val ID_TOKEN_KEY = "ID_TOKEN_KEY"
private const val CODE = "code"
private val GET_TOKEN_RESPONSE = InternalVKIDTokenPayloadResponse(
    accessToken = ACCESS_TOKEN_VALUE,
    refreshToken = REFRESH_TOKEN_NEW_VALUE,
    idToken = ID_TOKEN_VALUE,
    expiresIn = 0,
    userId = USER_ID,
    state = STATE,
    scope = "phone email",
)
private val EXCHANGE_TOKEN_RESPONSE = InternalVKIDCodePayloadResponse(
    code = CODE,
    state = STATE,
    deviceId = NEW_DEVICE_ID,
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
@DisplayName("Обмен v1 токена на v2")
@Priority(Priority.CRITICAL)
internal class VKIDExchangeTokenIntegrationTest : BaseUiTest() {

    @Test
    @AllureId("2303042")
    @DisplayName("Успешный обмен v1 токена на v2")
    fun exchangeTokenSuccess() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(USER_INFO_RESPONSE))
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE))
            .getTokenResponse(Result.success(GET_TOKEN_RESPONSE))
            .build()
        every { encryptedStorage.set(REFRESH_TOKEN_KEY, REFRESH_TOKEN_NEW_JSON) } just runs
        every { encryptedStorage.set(ACCESS_TOKEN_KEY, ACCESS_TOKEN_JSON) } just runs
        every { encryptedStorage.set(ID_TOKEN_KEY, ID_TOKEN_VALUE) } just runs
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        every { deviceIdStorage.setDeviceId(NEW_DEVICE_ID) } just runs
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = VKID.instance.exchangeToken(
                    v1Token = V1_TOKEN,
                    params = VKIDExchangeTokenParams {
                        state = STATE
                        codeExchangeState = STATE
                    }
                )
            }
        }
        step("Получен AT") {
            (result as? AccessToken?) shouldBe ACCESS_TOKEN
        }
        step("Не получена ошибка") {
            (result as? VKIDExchangeTokenFail?).shouldBeNull()
        }
    }

    @Test
    @AllureId("2303041")
    @DisplayName("Ошибка апи при обмене v1 токена на v2")
    fun apiCallFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .exchangeTokenResponse(Result.success(InternalVKIDCodePayloadResponse(error = "some error")))
            .build()
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = VKID.instance.exchangeToken(v1Token = V1_TOKEN, params = VKIDExchangeTokenParams { state = STATE })
            }
        }
        step("Не получен AT") {
            (result as? AccessToken?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDExchangeTokenFail?).shouldBeInstanceOf<VKIDExchangeTokenFail.FailedApiCall>()
        }
    }

    @Test
    @AllureId("2303129")
    @DisplayName("Ошибка получения токена при обмене v1 токена на v2")
    fun getTokenApiCallFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE))
            .getTokenResponse(Result.success(GET_TOKEN_RESPONSE.copy(error = "some error")))
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE))
            .build()
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        every { deviceIdStorage.setDeviceId(NEW_DEVICE_ID) } just runs
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = VKID.instance.exchangeToken(
                    v1Token = V1_TOKEN,
                    params = VKIDExchangeTokenParams {
                        state = STATE
                        codeExchangeState = STATE
                    }
                )
            }
        }
        step("Не получен AT") {
            (result as? AccessToken?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDExchangeTokenFail?).shouldBeInstanceOf<VKIDExchangeTokenFail.FailedApiCall>()
        }
    }

    @Test
    @AllureId("2303128")
    @DisplayName("Ошибка стейта получения токена при обмене v1 токена на v2")
    fun getTokenStateFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE))
            .getTokenResponse(Result.success(GET_TOKEN_RESPONSE.copy(state = "wrong state")))
            .build()
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        every { deviceIdStorage.setDeviceId(NEW_DEVICE_ID) } just runs
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = VKID.instance.exchangeToken(
                    v1Token = V1_TOKEN,
                    params = VKIDExchangeTokenParams {
                        state = STATE
                        codeExchangeState = STATE
                    }
                )
            }
        }
        step("Не получен AT") {
            (result as? AccessToken?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDExchangeTokenFail?) shouldBe VKIDExchangeTokenFail.FailedOAuthState("Invalid state during code exchange")
        }
    }

    @Test
    @AllureId("2303043")
    @DisplayName("Ошибка получения пользовательских данных при обмене v1 токена на v2")
    fun userDataApiCallFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(InternalVKIDUserInfoPayloadResponse(error = "some error")))
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE))
            .getTokenResponse(Result.success(GET_TOKEN_RESPONSE))
            .build()
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        every { deviceIdStorage.setDeviceId(NEW_DEVICE_ID) } just runs
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = VKID.instance.exchangeToken(
                    v1Token = V1_TOKEN,
                    params = VKIDExchangeTokenParams {
                        state = STATE
                        codeExchangeState = STATE
                    }
                )
            }
        }
        step("Не получен AT") {
            (result as? AccessToken?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDExchangeTokenFail?).shouldBeInstanceOf<VKIDExchangeTokenFail.FailedApiCall>()
        }
    }

    @Test
    @AllureId("2303040")
    @DisplayName("Ошибка неверного state при обмене v1 токена на v2")
    fun wrongStateFail() = run {
        val deviceIdStorage = mockk<InternalVKIDDeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<InternalVKIDPrefsStore>()
        val encryptedStorage = mockk<InternalVKIDPreferencesStorage>()
        InternalVKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .preferencesStorage(encryptedStorage)
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE.copy(state = "wrong state")))
            .build()
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = VKID.instance.exchangeToken(v1Token = V1_TOKEN, params = VKIDExchangeTokenParams { state = STATE })
            }
        }
        step("Не получен AT") {
            (result as? AccessToken?).shouldBeNull()
        }
        step("Получена ошибка") {
            (result as? VKIDExchangeTokenFail?) shouldBe VKIDExchangeTokenFail.FailedOAuthState("Invalid state during code receiving")
        }
    }

    private suspend fun VKID.exchangeToken(
        v1Token: String,
        params: VKIDExchangeTokenParams = VKIDExchangeTokenParams {}
    ): Any = suspendCoroutine {
        runBlocking {
            exchangeTokenToV2(
                v1Token = v1Token,
                callback = object : VKIDExchangeTokenCallback {
                    override fun onAuth(accessToken: AccessToken) = it.resume(accessToken)
                    override fun onFail(fail: VKIDExchangeTokenFail) = it.resume(fail)
                },
                params = params
            )
        }
    }
}
