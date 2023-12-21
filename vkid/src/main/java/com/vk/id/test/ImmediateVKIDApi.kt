package com.vk.id.test

import com.vk.id.commn.InternalVKIDApi
import com.vk.id.internal.api.VKIDApi

@InternalVKIDApi
internal class ImmediateVKIDApi(
    private val mockApi: OverrideVKIDApi
) : VKIDApi {
    override fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        clientSecret: String,
        deviceId: String,
        redirectUri: String
    ) = MockVKIDCall(
        mockApi.getToken(
            code = code,
            codeVerifier = codeVerifier,
            clientId = clientId,
            clientSecret = clientSecret,
            deviceId = deviceId,
            redirectUri = redirectUri
        )
    )

    override fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String
    ) = error("Not supported")
}