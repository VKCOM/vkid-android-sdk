package com.vk.id

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.vk.id.common.InternalVKIDApi
import kotlinx.parcelize.Parcelize

/**
 * Represents an access token for VK ID authentication.
 *
 * @since 0.0.1
 */
@Parcelize
@Immutable
public class AccessToken
@JvmOverloads @InternalVKIDApi
constructor(
    /**
     * The access token string.
     *
     * @since 0.0.1
     */
    public val token: String,
    /**
     * The ID token string.
     *
     * @since 2.0.0-alpha
     */
    public val idToken: String? = null,
    /**
     * The user ID associated with this token.
     *
     * @since 0.0.1
     */
    public val userID: Long,
    /**
     * The expiration time of the token. If the value is -1 the token will not expire.
     *
     * @since 0.0.2-alpha
     */
    public val expireTime: Long,
    /**
     * The user data associated with this token.
     *
     * @since 1.0.0
     */
    public val userData: VKIDUser,
    /**
     * The set of scopes granted for the [token].
     * Null only for tokens received with 2.0.0-alpha library release.
     * You're allowed to assert non-nullness the value if you're using a later version.
     *
     * @since 2.0.0-alpha02
     */
    public val scopes: Set<String>?,
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
