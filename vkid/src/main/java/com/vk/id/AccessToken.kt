package com.vk.id

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
public class AccessToken(
    public val token: String,
    public val userID: Long,
    public val expireTime: Long,
    public val userData: VKIDUser,
) : Parcelable {

    override fun hashCode(): Int {
        var result = token.hashCode()
        result = 31 * result + userID.hashCode()
        result = 31 * result + expireTime.hashCode()
        result = 31 * result + userData.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccessToken

        if (token != other.token) return false
        if (userID != other.userID) return false
        if (expireTime != other.expireTime) return false
        if (userData != other.userData) return false

        return true
    }
}
