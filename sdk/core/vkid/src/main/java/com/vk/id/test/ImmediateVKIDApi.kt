package com.vk.id.test

import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.VKIDApi

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
}
