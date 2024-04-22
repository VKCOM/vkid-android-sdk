package com.vk.id.test

import android.content.Context
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.device.VKIDDeviceIdProvider
import com.vk.id.internal.store.VKIDPrefsStore
import com.vk.id.storage.VKIDEncryptedSharedPreferencesStorage
import java.util.concurrent.atomic.AtomicInteger

@InternalVKIDApi
public class VKIDTestBuilder(
    private val context: Context,
) {
    private var deviceIdStorage: VKIDDeviceIdProvider.DeviceIdStorage? = null
    private var prefsStore: VKIDPrefsStore? = null
    private var encryptedSharedPreferencesStorage: VKIDEncryptedSharedPreferencesStorage? = null
    private var getTokenResponse = Result
        .failure<VKIDTokenPayloadResponse>(UnsupportedOperationException("Not supported"))
    private var refreshTokenResponse = Result
        .failure<VKIDTokenPayloadResponse>(UnsupportedOperationException("Not supported"))
    private var exchangeTokenResponse = Result
        .failure<VKIDCodePayloadResponse>(UnsupportedOperationException("Not supported"))
    private var getUserInfoResponses = listOf(
        Result.failure<VKIDUserInfoPayloadResponse>(UnsupportedOperationException("Not supported"))
    )
    private var logoutResponse = Result.success(VKIDLogoutPayloadResponse())
    private var mockApi: VKIDOverrideApi = object : VKIDOverrideApi {
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

    public fun refreshTokenResponse(response: Result<VKIDTokenPayloadResponse>): VKIDTestBuilder = apply {
        this.refreshTokenResponse = response
    }

    public fun getTokenResponse(response: Result<VKIDTokenPayloadResponse>): VKIDTestBuilder = apply {
        this.getTokenResponse = response
    }

    public fun getUserInfoResponse(response: Result<VKIDUserInfoPayloadResponse>): VKIDTestBuilder = apply {
        getUserInfoResponses(listOf(response))
    }

    public fun getUserInfoResponses(
        response1: Result<VKIDUserInfoPayloadResponse>,
        response2: Result<VKIDUserInfoPayloadResponse>,
    ): VKIDTestBuilder = getUserInfoResponses(listOf(response1, response2))

    private fun getUserInfoResponses(responses: List<Result<VKIDUserInfoPayloadResponse>>): VKIDTestBuilder = apply {
        this.getUserInfoResponses = responses
    }

    public fun logoutResponse(response: Result<VKIDLogoutPayloadResponse>): VKIDTestBuilder = apply {
        this.logoutResponse = response
    }

    public fun exchangeTokenResponse(response: Result<VKIDCodePayloadResponse>): VKIDTestBuilder = apply {
        this.exchangeTokenResponse = response
    }

    public fun overrideDeviceIdToNull(): VKIDTestBuilder = updateConfig { copy(overrideDeviceIdToNull = true) }
    public fun overrideState(state: String): VKIDTestBuilder = updateConfig { copy(overrideState = state) }
    public fun overrideOAuthToNull(): VKIDTestBuilder = updateConfig { copy(overrideOAuthToNull = true) }
    public fun user(user: VKIDUser): VKIDTestBuilder = updateConfig { copy(user = user) }
    public fun notifyNoBrowserAvailable(): VKIDTestBuilder = updateConfig { copy(notifyNoBrowserAvailable = true) }
    public fun notifyFailedRedirect(): VKIDTestBuilder = updateConfig { copy(notifyFailedRedirectActivity = true) }
    public fun requireUnsetUseAuthProviderIfPossible(): VKIDTestBuilder = updateConfig {
        copy(requireUnsetUseAuthProviderIfPossible = true)
    }

    private fun updateConfig(update: MockAuthProviderConfig.() -> MockAuthProviderConfig): VKIDTestBuilder = apply {
        authProviderConfig = authProviderConfig.update()
    }

    public fun deviceIdStorage(storage: VKIDDeviceIdProvider.DeviceIdStorage?): VKIDTestBuilder = apply {
        this.deviceIdStorage = storage
    }

    public fun prefsStore(store: VKIDPrefsStore?): VKIDTestBuilder = apply {
        this.prefsStore = store
    }

    public fun encryptedSharedPreferencesStorage(storage: VKIDEncryptedSharedPreferencesStorage?): VKIDTestBuilder = apply {
        this.encryptedSharedPreferencesStorage = storage
    }

    public fun build(): VKID = VKID(
        context = context,
        mockApi = mockApi,
        mockAuthProviderConfig = authProviderConfig,
        deviceIdStorage = deviceIdStorage,
        prefsStore = prefsStore,
        encryptedSharedPreferencesStorage = encryptedSharedPreferencesStorage,
    )
}
