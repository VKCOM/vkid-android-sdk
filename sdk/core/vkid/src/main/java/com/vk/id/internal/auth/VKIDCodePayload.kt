package com.vk.id.internal.auth

internal data class VKIDCodePayload(
    val code: String,
    val state: String,
    val deviceId: String,
)
