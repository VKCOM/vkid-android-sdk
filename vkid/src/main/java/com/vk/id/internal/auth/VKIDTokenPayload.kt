package com.vk.id.internal.auth

internal data class VKIDTokenPayload(
    val accessToken: String,
    val expiresIn: Long,
    val userId: Long,
    val email: String,
    val phone: String,
    val phoneAccessKey: String,
)