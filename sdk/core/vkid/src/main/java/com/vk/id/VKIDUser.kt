package com.vk.id

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.vk.silentauth.SilentAuthInfo
import kotlinx.parcelize.Parcelize

/**
 * Represents a user in the VK ID.
 *
 * @since 0.0.1
 */
@Suppress("LongParameterList")
@Parcelize
@Immutable
public class VKIDUser(
    /**
     * The first name of the user.
     *
     * @since 0.0.1
     */
    public val firstName: String,
    /**
     * The last name of the user.
     *
     * @since 0.0.1
     */
    public val lastName: String,
    /**
     * The phone number of the user, nullable.
     *
     * @since 0.0.1
     */
    public val phone: String? = null,
    /**
     * URL to the user's 50px photo, nullable.
     *
     * @since 0.0.1
     */
    public val photo50: String? = null,
    /**
     * URL to the user's 100px photo, nullable.
     *
     * @since 0.0.1
     */
    public val photo100: String? = null,
    /**
     * URL to the user's 200px photo, nullable.
     *
     * @since 0.0.1
     */
    public val photo200: String? = null,
    /**
     * The email of the user, nullable.
     *
     * @since 1.0.0
     */
    public val email: String? = null
) : Parcelable {

    /** @suppress */
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

    /** @suppress */
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

    /** @suppress */
    override fun toString(): String {
        return "VKIDUser(firstName='$firstName', lastName='$lastName', phone=$phone, photo50=$photo50, " +
            "photo100=$photo100, photo200=$photo200, email=$email)"
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
