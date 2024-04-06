@file:OptIn(InternalVKIDApi::class)

package com.vk.id.test

import com.google.gson.annotations.SerializedName
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface OverrideVKIDApi {
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
}

@InternalVKIDApi
public data class VKIDTokenPayloadResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("id_token")
    val idToken: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    @SerializedName("user_id")
    val userId: Long,
)

@InternalVKIDApi
public data class VKIDUserInfoPayloadResponse(
    val user: VKIDUserPayloadResponse,
    val state: String,
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
