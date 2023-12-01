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
) : Parcelable {

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (photo50?.hashCode() ?: 0)
        result = 31 * result + (photo100?.hashCode() ?: 0)
        result = 31 * result + (photo200?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VKIDUser

        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (phone != other.phone) return false
        if (photo50 != other.photo50) return false
        if (photo100 != other.photo100) return false
        if (photo200 != other.photo200) return false
        if (email != other.email) return false

        return true
    }
}

internal fun SilentAuthInfo.toVKIDUser() = VKIDUser(
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    photo50 = photo50,
    photo100 = photo100,
    photo200 = photo200,
)
