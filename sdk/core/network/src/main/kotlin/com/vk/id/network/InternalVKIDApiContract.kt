package com.vk.id.network

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.http.HttpResponse
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
    ): InternalVKIDCall<HttpResponse>

    public fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String,
    ): InternalVKIDCall<HttpResponse>

    public fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String,
    ): InternalVKIDCall<HttpResponse>

    public fun getUser(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): InternalVKIDCall<HttpResponse>

    public fun exchangeToken(
        v1Token: String,
        clientId: String,
        deviceId: String,
        state: String,
        codeChallenge: String,
    ): InternalVKIDCall<HttpResponse>

    public fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): InternalVKIDCall<HttpResponse>

    public fun sendStatEventsAnonymously(
        clientId: String,
        clientSecret: String,
        sakVersion: String,
        events: JSONArray,
        externalDeviceId: String,
    ): InternalVKIDCall<HttpResponse>

    @Suppress("LongParameterList")
    public fun sendStatEvents(
        accessToken: String,
        clientId: String,
        clientSecret: String,
        sakVersion: String,
        events: JSONArray,
        externalDeviceId: String,
    ): InternalVKIDCall<HttpResponse>
}
