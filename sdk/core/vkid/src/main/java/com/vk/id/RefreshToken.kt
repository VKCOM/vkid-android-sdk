package com.vk.id

import com.vk.id.common.InternalVKIDApi

/**
 * Represents a refresh token for VK ID authentication.
 *
 * @property token The refresh token string.
 * @property scopes The scopes granted for the refresh token.
 * That means that access tokens received with this token will have these scopes.
 * Null only for tokens received with 2.0.0-alpha library release.
 * You're allowed to assert non-nullness the value if you're using a later version.
 */
public class RefreshToken @InternalVKIDApi constructor(
    public val token: String,
    public val scopes: Set<String>?,
) {
    /** @suppress */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RefreshToken

        if (token != other.token) return false
        if (scopes != other.scopes) return false

        return true
    }

    /** @suppress */
    override fun hashCode(): Int {
        var result = token.hashCode()
        result = 31 * result + (scopes?.hashCode() ?: 0)
        return result
    }
}
