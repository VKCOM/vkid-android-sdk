package com.vk.id.internal.api

import okhttp3.Call

internal interface VKIDApi {

    @Suppress("LongParameterList")
    fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        deviceId: String,
        redirectUri: String,
        state: String,
    ): Call

    fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String,
    ): Call

    fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String,
    ): Call

    fun getUser(
        idToken: String,
        clientId: String,
        deviceId: String,
        state: String,
    ): Call

    fun exchangeToken(
        v1Token: String,
        clientId: String,
        deviceId: String,
        state: String,
    ): Call

    fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): Call
}
