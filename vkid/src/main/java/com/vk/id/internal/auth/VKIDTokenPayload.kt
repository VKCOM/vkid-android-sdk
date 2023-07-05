package com.vk.id.internal.auth

import java.util.concurrent.TimeUnit

internal data class VKIDTokenPayload(
    val accessToken: String,
    val expiresIn: Long,
    val userId: Long,
    val email: String,
    val phone: String,
    val phoneAccessKey: String,
)

/**
 * Converts "expiresIn" type of time to actual expire time
 * returns -1 if this <= 0
 */
internal inline val Long.toExpireTime get() =
    if (this > 0) {
        System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(this)
    } else {
        -1
    }