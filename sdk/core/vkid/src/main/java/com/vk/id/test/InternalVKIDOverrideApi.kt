@file:OptIn(InternalVKIDApi::class)

package com.vk.id.test

import com.google.gson.annotations.SerializedName
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface InternalVKIDOverrideApi {

    public fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String
    ): Result<InternalVKIDTokenPayloadResponse>

    public fun exchangeToken(
        v1Token: String,
        clientId: String,
        deviceId: String,
        state: String,
        codeChallenge: String,
    ): Result<InternalVKIDCodePayloadResponse>

    @Suppress("LongParameterList")
    public fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        deviceId: String,
        redirectUri: String,
        state: String,
    ): Result<InternalVKIDTokenPayloadResponse>

    public fun getUserInfo(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): Result<InternalVKIDUserInfoPayloadResponse>

    public fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): Result<InternalVKIDLogoutPayloadResponse>
}

@InternalVKIDApi
public data class InternalVKIDTokenPayloadResponse(
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("refresh_token")
    val refreshToken: String? = null,
    @SerializedName("id_token")
    val idToken: String? = null,
    @SerializedName("expires_in")
    val expiresIn: Long? = null,
    @SerializedName("user_id")
    val userId: Long? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("scope")
    val scope: String? = null,
)

@InternalVKIDApi
public data class InternalVKIDCodePayloadResponse(
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("device_id")
    val deviceId: String? = null,
    @SerializedName("error")
    val error: String? = null,
)

@InternalVKIDApi
public data class InternalVKIDUserInfoPayloadResponse(
    val error: String? = null,
    val user: InternalVKIDUserPayloadResponse? = null,
    val state: String? = null,
)

@InternalVKIDApi
public data class InternalVKIDLogoutPayloadResponse(
    val error: String? = null,
)

@InternalVKIDApi
public data class InternalVKIDUserPayloadResponse(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("avatar")
    val avatar: String,
)

@InternalVKIDApi
public data class InternalVKIDSilentAuthProvidersResponse(
    @SerializedName("response")
    val response: String
)
