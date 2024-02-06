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
        clientSecret: String,
        deviceId: String,
        redirectUri: String
    ): Result<VKIDTokenPayloadResponse>
}

@InternalVKIDApi
public data class VKIDTokenPayloadResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("phone_access_key")
    val phoneAccessKey: String,
)

@InternalVKIDApi
public data class VKIDSilentAuthProvidersResponse(
    @SerializedName("response")
    val response: String
)
