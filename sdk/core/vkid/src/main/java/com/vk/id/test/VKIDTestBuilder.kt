package com.vk.id.test

import android.content.Context
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.commn.InternalVKIDApi

@InternalVKIDApi
public class VKIDTestBuilder(
    private val context: Context
) {
    private var getTokenResponse = Result
        .failure<VKIDTokenPayloadResponse>(UnsupportedOperationException("Not supported"))
    private var mockApi: OverrideVKIDApi = object : OverrideVKIDApi {
        override fun getToken(
            code: String,
            codeVerifier: String,
            clientId: String,
            clientSecret: String,
            deviceId: String,
            redirectUri: String
        ) = getTokenResponse
    }
    private var authProviderConfig: MockAuthProviderConfig = MockAuthProviderConfig()

    public fun getTokenResponse(response: Result<VKIDTokenPayloadResponse>): VKIDTestBuilder = apply { this.getTokenResponse = response }
    public fun overrideUuid(uuid: String): VKIDTestBuilder = updateConfig { copy(overrideUuid = uuid) }
    public fun overrideState(state: String): VKIDTestBuilder = updateConfig { copy(overrideState = state) }
    public fun overrideOAuthToNull(): VKIDTestBuilder = updateConfig { copy(overrideOAuthToNull = true) }
    public fun user(user: VKIDUser): VKIDTestBuilder = updateConfig { copy(user = user) }
    public fun notifyNoBrowserAvailable(): VKIDTestBuilder = updateConfig { copy(notifyNoBrowserAvailable = true) }
    public fun notifyFailedRedirect(): VKIDTestBuilder = updateConfig { copy(notifyFailedRedirectActivity = true) }
    public fun requireUnsetUseAuthProviderIfPossible(): VKIDTestBuilder = updateConfig {
        copy(
            requireUnsetUseAuthProviderIfPossible = true
        )
    }

    private fun updateConfig(update: MockAuthProviderConfig.() -> MockAuthProviderConfig): VKIDTestBuilder = apply {
        authProviderConfig = authProviderConfig.update()
    }

    public fun build(): VKID = VKID(
        context = context,
        mockApi = mockApi,
        mockAuthProviderConfig = authProviderConfig
    )
}
