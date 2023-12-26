package com.vk.id.internal.api

import okhttp3.Call

internal interface VKIDApi {

    @Suppress("LongParameterList")
    fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        clientSecret: String,
        deviceId: String,
        redirectUri: String,
    ): Call

    fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String,
    ): Call
}
