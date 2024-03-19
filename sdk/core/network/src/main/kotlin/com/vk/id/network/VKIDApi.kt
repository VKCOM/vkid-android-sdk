package com.vk.id.network

import com.vk.id.common.InternalVKIDApi
import okhttp3.Call

@InternalVKIDApi
public interface VKIDApi {

    @Suppress("LongParameterList")
    public fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        clientSecret: String,
        deviceId: String,
        redirectUri: String,
    ): Call

    public fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String,
    ): Call
}
