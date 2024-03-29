package com.vk.id.internal.api.dto

internal data class VKIDUserInfoPayload(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val avatar: String,
    val email: String,
)
