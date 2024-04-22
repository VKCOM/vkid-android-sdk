@file:OptIn(InternalVKIDApi::class)

package com.vk.id.test

import com.google.gson.annotations.SerializedName
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface VKIDOverrideApi {

    public fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String
    ): Result<VKIDTokenPayloadResponse>

    public fun exchangeToken(
        v1Token: String,
        clientId: String,
        deviceId: String,
        state: String,
        codeChallenge: String,
    ): Result<VKIDCodePayloadResponse>

    @Suppress("LongParameterList")
    public fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        deviceId: String,
        redirectUri: String,
        state: String,
    ): Result<VKIDTokenPayloadResponse>

    public fun getUserInfo(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): Result<VKIDUserInfoPayloadResponse>

    public fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): Result<VKIDLogoutPayloadResponse>
}

@InternalVKIDApi
public data class VKIDTokenPayloadResponse(
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
)

@InternalVKIDApi
public data class VKIDCodePayloadResponse(
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("error")
    val error: String? = null,
)

@InternalVKIDApi
public data class VKIDUserInfoPayloadResponse(
    val error: String? = null,
    val user: VKIDUserPayloadResponse? = null,
    val state: String? = null,
)

@InternalVKIDApi
public data class VKIDLogoutPayloadResponse(
    val error: String? = null,
)

@InternalVKIDApi
public data class VKIDUserPayloadResponse(
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
public data class VKIDSilentAuthProvidersResponse(
    @SerializedName("response")
    val response: String
)
