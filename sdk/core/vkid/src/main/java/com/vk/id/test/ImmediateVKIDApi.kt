package com.vk.id.test

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.VKIDApi
import org.json.JSONArray

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
    ) = MockVKIDCall(Result.success(VKIDSilentAuthProvidersResponse("null")))

    override fun sendStatEventsAnonymously(
        clientId: String,
        clientSecret: String,
        events: JSONArray,
    ) = MockVKIDCall(Result.success(VKIDSilentAuthProvidersResponse("null")))
}
