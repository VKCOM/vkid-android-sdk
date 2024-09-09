package com.vk.id.test

import android.content.Context
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.context.InternalVKIDActivityStarter
import com.vk.id.internal.context.InternalVKIDPackageManager
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
    private var getSilentAuthProvidersResponse = Result.success(InternalVKIDSilentAuthProvidersResponse(emptyList()))
    private var packageManager: InternalVKIDPackageManager? = null
    private var activityStarter: InternalVKIDActivityStarter? = null

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

        override fun getSilentAuthProviders(clientId: String, clientSecret: String) = getSilentAuthProvidersResponse
    }

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

    public fun getSilentAuthProviders(response: Result<InternalVKIDSilentAuthProvidersResponse>): InternalVKIDTestBuilder = apply {
        this.getSilentAuthProvidersResponse = response
    }

    private fun getUserInfoResponses(responses: List<Result<InternalVKIDUserInfoPayloadResponse>>): InternalVKIDTestBuilder = apply {
        this.getUserInfoResponses = responses
    }

    public fun logoutResponse(response: Result<InternalVKIDLogoutPayloadResponse>): InternalVKIDTestBuilder = apply {
        this.logoutResponse = response
    }

    public fun exchangeTokenResponse(response: Result<InternalVKIDCodePayloadResponse>): InternalVKIDTestBuilder = apply {
        this.exchangeTokenResponse = response
    }

    public fun overridePackageManager(pm: InternalVKIDPackageManager): InternalVKIDTestBuilder = apply {
        this.packageManager = pm
    }

    public fun overrideActivityStarter(starter: InternalVKIDActivityStarter): InternalVKIDTestBuilder = apply {
        this.activityStarter = starter
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
            deviceIdStorage = deviceIdStorage,
            prefsStore = prefsStore,
            encryptedSharedPreferencesStorage = encryptedSharedPreferencesStorage,
            packageManager = packageManager,
            activityStarter = activityStarter,
        )
    }
}
