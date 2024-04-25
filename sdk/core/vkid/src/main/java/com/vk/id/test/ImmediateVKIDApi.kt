package com.vk.id.test

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.VKIDApi

@InternalVKIDApi
internal class ImmediateVKIDApi(
    private val mockApi: OverrideVKIDApi
) : VKIDApi {

    override fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        deviceId: String,
        redirectUri: String,
        state: String,
    ) = MockVKIDCall(
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
    ) = MockVKIDCall(Result.success(VKIDSilentAuthProvidersResponse("null")))

    override fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String
    ) = MockVKIDCall(
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
    ) = MockVKIDCall(
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
        state: String
    ) = MockVKIDCall(Result.failure<Unit>(RuntimeException("Not supported")))

    override fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String
    ) = MockVKIDCall(
        mockApi.logout(
            accessToken = accessToken,
            clientId = clientId,
            deviceId = deviceId,
        )
    )
}
