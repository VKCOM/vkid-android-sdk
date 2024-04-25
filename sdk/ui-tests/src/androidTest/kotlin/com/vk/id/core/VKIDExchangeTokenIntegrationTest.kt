@file:OptIn(InternalVKIDApi::class)

package com.vk.id.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vk.id.AccessToken
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
import com.vk.id.exchangetoken.VKIDExchangeTokenCallback
import com.vk.id.exchangetoken.VKIDExchangeTokenFail
import com.vk.id.exchangetoken.VKIDExchangeTokenParams
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.store.PrefsStore
import com.vk.id.storage.EncryptedSharedPreferencesStorage
import com.vk.id.test.VKIDCodePayloadResponse
import com.vk.id.test.VKIDTestBuilder
import com.vk.id.test.VKIDTokenPayloadResponse
import com.vk.id.test.VKIDUserInfoPayloadResponse
import com.vk.id.test.VKIDUserPayloadResponse
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
private const val REFRESH_TOKEN_NEW_VALUE = "refresh token new"
private const val ID_TOKEN_VALUE = "id token"
private const val USER_ID = 123L
private const val STATE = "state"
private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
private const val ID_TOKEN_KEY = "ID_TOKEN_KEY"
private const val CODE = "code"
private val GET_TOKEN_RESPONSE = VKIDTokenPayloadResponse(
    accessToken = ACCESS_TOKEN_VALUE,
    refreshToken = REFRESH_TOKEN_NEW_VALUE,
    idToken = ID_TOKEN_VALUE,
    expiresIn = 0,
    userId = USER_ID,
    state = STATE,
)
private val EXCHANGE_TOKEN_RESPONSE = VKIDCodePayloadResponse(
    code = CODE,
    state = STATE,
)
private val USER_INFO_RESPONSE = VKIDUserInfoPayloadResponse(
    user = VKIDUserPayloadResponse(
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
    )
)

@Platform(Platform.ANDROID_MANUAL)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.DANIIL_KLIMCHUK)
@Feature(TestFeature.OAUTH_2_1)
@RunWith(AndroidJUnit4::class)
@DisplayName("Обмен v1 токена на v2")
internal class VKIDExchangeTokenIntegrationTest : BaseUiTest() {

    @Test
    @AllureId("2303042")
    @DisplayName("Успешный обмен v1 токена на v2")
    fun exchangeTokenSuccess() = run {
        val deviceIdStorage = mockk<DeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<PrefsStore>()
        val encryptedStorage = mockk<EncryptedSharedPreferencesStorage>()
        val vkid = VKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(USER_INFO_RESPONSE))
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE))
            .getTokenResponse(Result.success(GET_TOKEN_RESPONSE))
            .build()
        every { encryptedStorage.set(REFRESH_TOKEN_KEY, REFRESH_TOKEN_NEW_VALUE) } just runs
        every { encryptedStorage.set(ACCESS_TOKEN_KEY, ACCESS_TOKEN_JSON) } just runs
        every { encryptedStorage.set(ID_TOKEN_KEY, ID_TOKEN_VALUE) } just runs
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = vkid.exchangeToken(
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
        val deviceIdStorage = mockk<DeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<PrefsStore>()
        val encryptedStorage = mockk<EncryptedSharedPreferencesStorage>()
        val vkid = VKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .exchangeTokenResponse(Result.success(VKIDCodePayloadResponse(error = "some error")))
            .build()
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = vkid.exchangeToken(v1Token = V1_TOKEN, params = VKIDExchangeTokenParams { state = STATE })
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
        val deviceIdStorage = mockk<DeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<PrefsStore>()
        val encryptedStorage = mockk<EncryptedSharedPreferencesStorage>()
        val vkid = VKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(VKIDUserInfoPayloadResponse(error = "some error")))
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE))
            .build()
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = vkid.exchangeToken(
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
    @AllureId("2303128z")
    @DisplayName("Ошибка стейта получения токена при обмене v1 токена на v2")
    fun getTokenStateFail() = run {
        val deviceIdStorage = mockk<DeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<PrefsStore>()
        val encryptedStorage = mockk<EncryptedSharedPreferencesStorage>()
        val vkid = VKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE))
            .getTokenResponse(Result.success(GET_TOKEN_RESPONSE.copy(state = "wrong state")))
            .build()
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = vkid.exchangeToken(
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
        val deviceIdStorage = mockk<DeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<PrefsStore>()
        val encryptedStorage = mockk<EncryptedSharedPreferencesStorage>()
        val vkid = VKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(VKIDUserInfoPayloadResponse(error = "some error")))
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE))
            .getTokenResponse(Result.success(GET_TOKEN_RESPONSE))
            .build()
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = vkid.exchangeToken(
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
        val deviceIdStorage = mockk<DeviceIdProvider.DeviceIdStorage>()
        val prefsStore = mockk<PrefsStore>()
        val encryptedStorage = mockk<EncryptedSharedPreferencesStorage>()
        val vkid = VKIDTestBuilder(InstrumentationRegistry.getInstrumentation().context)
            .deviceIdStorage(deviceIdStorage)
            .prefsStore(prefsStore)
            .encryptedSharedPreferencesStorage(encryptedStorage)
            .getUserInfoResponse(Result.success(USER_INFO_RESPONSE))
            .exchangeTokenResponse(Result.success(EXCHANGE_TOKEN_RESPONSE.copy(state = "wrong state")))
            .build()
        every { prefsStore.clear() } just runs
        every { deviceIdStorage.getDeviceId() } returns "device id"
        var result: Any? = null
        step("Обменивается токен") {
            runBlocking {
                result = vkid.exchangeToken(v1Token = V1_TOKEN, params = VKIDExchangeTokenParams { state = STATE })
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
                    override fun onSuccess(accessToken: AccessToken) = it.resume(accessToken)
                    override fun onFail(fail: VKIDExchangeTokenFail) = it.resume(fail)
                },
                params = params
            )
        }
    }
}
