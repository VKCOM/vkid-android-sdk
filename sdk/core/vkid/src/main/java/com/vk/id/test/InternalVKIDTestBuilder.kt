package com.vk.id.test

import android.content.Context
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.groupsubscription.GroupSubscriptionLimit
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.context.InternalVKIDActivityStarter
import com.vk.id.internal.context.InternalVKIDPackageManager
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.network.groupsubscription.InternalVKIDGroupSubscriptionApiContract
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupByIdData
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupMembersData
import com.vk.id.storage.InternalVKIDPreferencesStorage
import java.util.concurrent.atomic.AtomicInteger

@InternalVKIDApi
public class InternalVKIDTestBuilder(
    private val context: Context,
) {
    private var deviceIdStorage: InternalVKIDDeviceIdProvider.DeviceIdStorage? = null
    private var prefsStore: InternalVKIDPrefsStore? = null
    private var preferencesStorage: InternalVKIDPreferencesStorage? = null
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

    private var shouldShowSubscriptionResponse = Result.failure<Boolean>(UnsupportedOperationException("Not mocked"))
    private var isServiceAccountResponse = Result.failure<Boolean>(UnsupportedOperationException("Not mocked"))
    private var getGroupResponse = Result.failure<InternalVKIDGroupByIdData>(UnsupportedOperationException("Not mocked"))
    private var getMembersResponses =
        listOf(Result.failure<InternalVKIDGroupMembersData>(UnsupportedOperationException("Not mocked")))
    private var subscribeToGroupResponses = listOf(Result.failure<Unit>(UnsupportedOperationException("Not mocked")))
    private var groupSubscriptionLimit: GroupSubscriptionLimit? = null
    private val mockGroupSubscriptionApi = object : InternalVKIDGroupSubscriptionApiContract {
        override suspend fun shouldShowSubscription(accessToken: String): Boolean {
            return shouldShowSubscriptionResponse.getOrThrow()
        }

        override suspend fun isServiceAccount(accessToken: String): Boolean {
            return isServiceAccountResponse.getOrThrow()
        }

        override suspend fun getGroup(accessToken: String, groupId: String): InternalVKIDGroupByIdData {
            return getGroupResponse.getOrThrow()
        }

        private val getMembersResponseIndex = AtomicInteger(0)

        override suspend fun getMembers(accessToken: String, groupId: String, justFriends: Boolean): InternalVKIDGroupMembersData {
            return getMembersResponses[getMembersResponseIndex.getAndIncrement()].getOrThrow()
        }

        private val subscribeToGroupResponseIndex = AtomicInteger(0)

        override suspend fun subscribeToGroup(accessToken: String, groupId: String) {
            return subscribeToGroupResponses[subscribeToGroupResponseIndex.getAndIncrement()].getOrThrow()
        }
    }

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

    public fun preferencesStorage(storage: InternalVKIDPreferencesStorage?): InternalVKIDTestBuilder = apply {
        this.preferencesStorage = storage
    }

    public fun shouldShowSubscriptionResponse(response: Result<Boolean>): InternalVKIDTestBuilder = apply {
        shouldShowSubscriptionResponse = response
    }

    public fun isServiceAccountResponse(response: Result<Boolean>): InternalVKIDTestBuilder = apply {
        isServiceAccountResponse = response
    }

    public fun getGroupResponse(response: Result<InternalVKIDGroupByIdData>): InternalVKIDTestBuilder = apply {
        getGroupResponse = response
    }

    public fun getMembersResponses(
        response1: Result<InternalVKIDGroupMembersData>,
        response2: Result<InternalVKIDGroupMembersData>,
    ): InternalVKIDTestBuilder = apply {
        getMembersResponses = listOf(response1, response2)
    }

    public fun subscribeToGroupResponse(response: Result<Unit>): InternalVKIDTestBuilder = apply {
        subscribeToGroupResponses = listOf(response)
    }

    public fun subscribeToGroupResponses(responses: List<Result<Unit>>): InternalVKIDTestBuilder = apply {
        subscribeToGroupResponses = responses
    }

    public fun groupSubscriptionLimit(limit: GroupSubscriptionLimit): InternalVKIDTestBuilder = apply {
        groupSubscriptionLimit = limit
    }

    public fun build() {
        VKID.init(
            context = context,
            mockApi = mockApi,
            groupSubscriptionApiContract = mockGroupSubscriptionApi,
            deviceIdStorage = deviceIdStorage,
            prefsStore = prefsStore,
            encryptedSharedPreferencesStorage = preferencesStorage,
            packageManager = packageManager,
            activityStarter = activityStarter,
            groupSubscriptionLimit = groupSubscriptionLimit,
        )
    }
}
