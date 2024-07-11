package com.vk.id.test

import android.content.Context
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.storage.InternalVKIDEncryptedSharedPreferencesStorage
import java.util.concurrent.atomic.AtomicInteger

@InternalVKIDApi
public class InternalVKIDTestBuilder(
    private val context: Context,
) {
    private var deviceIdStorage: InternalVKIDDeviceIdProvider.DeviceIdStorage? = null
    private var prefsStore: InternalVKIDPrefsStore? = null
    private var encryptedSharedPreferencesStorage: InternalVKIDEncryptedSharedPreferencesStorage? = null
    private var getTokenResponse = Result
        .failure<InternalVKIDTokenPayloadResponse>(UnsupportedOperationException("Not supported"))
    private var refreshTokenResponse = Result
        .failure<InternalVKIDTokenPayloadResponse>(UnsupportedOperationException("Not supported"))
    private var exchangeTokenResponse = Result
        .failure<InternalVKIDCodePayloadResponse>(UnsupportedOperationException("Not supported"))
    private var getUserInfoResponses = listOf(
        Result.failure<InternalVKIDUserInfoPayloadResponse>(UnsupportedOperationException("Not supported"))
    )
    private var logoutResponse = Result.success(InternalVKIDLogoutPayloadResponse())
    private var mockApi: InternalVKIDOverrideApi = object : InternalVKIDOverrideApi {
        override fun refreshToken(
            refreshToken: String,
            clientId: String,
            deviceId: String,
            state: String,
        ) = refreshTokenResponse

        override fun exchangeToken(
            v1Token: String,
            clientId: String,
            deviceId: String,
            state: String,
            codeChallenge: String,
        ) = exchangeTokenResponse

        override fun getToken(
            code: String,
            codeVerifier: String,
            clientId: String,
            deviceId: String,
            redirectUri: String,
            state: String,
        ) = getTokenResponse

        private val userInfoResponseIndex = AtomicInteger(0)

        override fun getUserInfo(
            accessToken: String,
            clientId: String,
            deviceId: String,
        ) = getUserInfoResponses[userInfoResponseIndex.getAndIncrement()]

        override fun logout(
            accessToken: String,
            clientId: String,
            deviceId: String,
        ) = logoutResponse
    }
    private var authProviderConfig: MockAuthProviderConfig = MockAuthProviderConfig()

    public fun refreshTokenResponse(response: Result<InternalVKIDTokenPayloadResponse>): InternalVKIDTestBuilder = apply {
        this.refreshTokenResponse = response
    }

    public fun getTokenResponse(response: Result<InternalVKIDTokenPayloadResponse>): InternalVKIDTestBuilder = apply {
        this.getTokenResponse = response
    }

    public fun getUserInfoResponse(response: Result<InternalVKIDUserInfoPayloadResponse>): InternalVKIDTestBuilder = apply {
        getUserInfoResponses(listOf(response))
    }

    public fun getUserInfoResponses(
        response1: Result<InternalVKIDUserInfoPayloadResponse>,
        response2: Result<InternalVKIDUserInfoPayloadResponse>,
    ): InternalVKIDTestBuilder = getUserInfoResponses(listOf(response1, response2))

    private fun getUserInfoResponses(responses: List<Result<InternalVKIDUserInfoPayloadResponse>>): InternalVKIDTestBuilder = apply {
        this.getUserInfoResponses = responses
    }

    public fun logoutResponse(response: Result<InternalVKIDLogoutPayloadResponse>): InternalVKIDTestBuilder = apply {
        this.logoutResponse = response
    }

    public fun exchangeTokenResponse(response: Result<InternalVKIDCodePayloadResponse>): InternalVKIDTestBuilder = apply {
        this.exchangeTokenResponse = response
    }

    public fun overrideDeviceId(deviceId: String?): InternalVKIDTestBuilder = updateConfig { copy(deviceId = deviceId) }
    public fun overrideState(state: String): InternalVKIDTestBuilder = updateConfig { copy(overrideState = state) }
    public fun overrideOAuthToNull(): InternalVKIDTestBuilder = updateConfig { copy(overrideOAuthToNull = true) }
    public fun user(user: VKIDUser): InternalVKIDTestBuilder = updateConfig { copy(user = user) }
    public fun notifyNoBrowserAvailable(): InternalVKIDTestBuilder = updateConfig { copy(notifyNoBrowserAvailable = true) }
    public fun notifyFailedRedirect(): InternalVKIDTestBuilder = updateConfig { copy(notifyFailedRedirectActivity = true) }
    public fun requireUnsetUseAuthProviderIfPossible(): InternalVKIDTestBuilder = updateConfig {
        copy(requireUnsetUseAuthProviderIfPossible = true)
    }

    private fun updateConfig(update: MockAuthProviderConfig.() -> MockAuthProviderConfig): InternalVKIDTestBuilder = apply {
        authProviderConfig = authProviderConfig.update()
    }

    public fun deviceIdStorage(storage: InternalVKIDDeviceIdProvider.DeviceIdStorage?): InternalVKIDTestBuilder = apply {
        this.deviceIdStorage = storage
    }

    public fun prefsStore(store: InternalVKIDPrefsStore?): InternalVKIDTestBuilder = apply {
        this.prefsStore = store
    }

    public fun encryptedSharedPreferencesStorage(storage: InternalVKIDEncryptedSharedPreferencesStorage?): InternalVKIDTestBuilder = apply {
        this.encryptedSharedPreferencesStorage = storage
    }

    public fun build() {
        VKID.init(
            context = context,
            mockApi = mockApi,
            mockAuthProviderConfig = authProviderConfig,
            deviceIdStorage = deviceIdStorage,
            prefsStore = prefsStore,
            encryptedSharedPreferencesStorage = encryptedSharedPreferencesStorage,
        )
    }
}
