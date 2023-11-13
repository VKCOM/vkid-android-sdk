package com.vk.id

import com.vk.silentauth.SilentAuthInfo

public data class VKIDUser(
    val firstName: String,
    val lastName: String,
    val phone: String? = null,
    val photo50: String? = null,
    val photo100: String? = null,
    val photo200: String? = null,
)

internal fun SilentAuthInfo.toVKIDUser() = VKIDUser(
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    photo50 = photo50,
    photo100 = photo100,
    photo200 = photo200,
)
