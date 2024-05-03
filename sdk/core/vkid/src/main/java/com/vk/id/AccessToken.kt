package com.vk.id

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

/**
 * Represents an access token for VK ID authentication.
 *
 * @property token The access token string.
 * @property idToken The ID token string.
 * @property userID The user ID associated with this token.
 * @property expireTime The expiration time of the token. If the value is -1 the token will not expire.
 * @property userData The user data associated with this token.
 */
@Parcelize
@Immutable
public class AccessToken @JvmOverloads constructor(
    public val token: String,
    public val idToken: String? = null,
    public val userID: Long,
    public val expireTime: Long,
    public val userData: VKIDUser,
) : Parcelable {

    /** @suppress */
    override fun hashCode(): Int {
        var result = token.hashCode()
        result = 31 * result + userID.hashCode()
        result = 31 * result + expireTime.hashCode()
        result = 31 * result + userData.hashCode()
        return result
    }

    /** @suppress */
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
