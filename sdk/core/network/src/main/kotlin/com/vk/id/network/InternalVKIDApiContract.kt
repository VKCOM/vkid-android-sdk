package com.vk.id.network

import com.vk.id.common.InternalVKIDApi
import okhttp3.Call
import org.json.JSONArray

@InternalVKIDApi
public interface InternalVKIDApiContract {

    @Suppress("LongParameterList")
    public fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        deviceId: String,
        redirectUri: String,
        state: String,
    ): Call

    public fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String,
    ): Call

    public fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String,
    ): Call

    public fun getUser(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): Call

    public fun exchangeToken(
        v1Token: String,
        clientId: String,
        deviceId: String,
        state: String,
        codeChallenge: String,
    ): Call

    public fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): Call

    public fun sendStatEventsAnonymously(
        clientId: String,
        clientSecret: String,
        sakVersion: String,
        events: JSONArray
    ): Call

    public fun sendStatEvents(
        accessToken: String,
        clientId: String,
        clientSecret: String,
        sakVersion: String,
        events: JSONArray
    ): Call
}
