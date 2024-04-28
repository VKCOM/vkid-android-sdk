package com.vk.id.test

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.InternalVKIDApiContract

@InternalVKIDApi
internal class InternalVKIDImmediateApi(
    private val mockApi: InternalVKIDOverrideApi
) : InternalVKIDApiContract {

    override fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        deviceId: String,
        redirectUri: String,
        state: String,
    ) = VKIDMockCall(
        mockApi.getToken(
            code = code,
            codeVerifier = codeVerifier,
            clientId = clientId,
            deviceId = deviceId,
            redirectUri = redirectUri,
            state = state,
        )
    )

    override fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String
    ) = VKIDMockCall(Result.success(InternalVKIDSilentAuthProvidersResponse("null")))

    override fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String
    ) = VKIDMockCall(
        mockApi.refreshToken(
            refreshToken = refreshToken,
            clientId = clientId,
            deviceId = deviceId,
            state = state,
        )
    )

    override fun getUser(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ) = VKIDMockCall(
        mockApi.getUserInfo(
            accessToken = accessToken,
            clientId = clientId,
            deviceId = deviceId,
        )
    )

    override fun exchangeToken(
        v1Token: String,
        clientId: String,
        deviceId: String,
        state: String,
        codeChallenge: String,
    ) = VKIDMockCall(
        mockApi.exchangeToken(
            v1Token = v1Token,
            clientId = clientId,
            deviceId = deviceId,
            state = state,
            codeChallenge = codeChallenge,
        )
    )

    override fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String
    ) = VKIDMockCall(
        mockApi.logout(
            accessToken = accessToken,
            clientId = clientId,
            deviceId = deviceId,
        )
    )
}
