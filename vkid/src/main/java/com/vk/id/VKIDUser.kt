package com.vk.id

import android.os.Parcelable
import com.vk.silentauth.SilentAuthInfo
import kotlinx.parcelize.Parcelize

@Suppress("LongParameterList")
@Parcelize
public class VKIDUser(
    public val firstName: String,
    public val lastName: String,
    public val phone: String? = null,
    public val photo50: String? = null,
    public val photo100: String? = null,
    public val photo200: String? = null,
    public val email: String? = null
) : Parcelable

internal fun SilentAuthInfo.toVKIDUser() = VKIDUser(
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    photo50 = photo50,
    photo100 = photo100,
    photo200 = photo200,
)
