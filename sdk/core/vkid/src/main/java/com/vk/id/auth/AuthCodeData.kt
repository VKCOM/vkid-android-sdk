package com.vk.id.auth

/**
 * The data with result of the first step of auth.
 *
 * @property code Auth code.
 * @property deviceId Device id to be passed to /auth when exchanging [code] for token.
 */
public class AuthCodeData @JvmOverloads constructor(
    public val code: String,
    public val deviceId: String = "",
) {
    /** @suppress */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AuthCodeData

        if (code != other.code) return false
        if (deviceId != other.deviceId) return false

        return true
    }

    /** @suppress */
    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + deviceId.hashCode()
        return result
    }

    /** @suppress */
    override fun toString(): String {
        return "AuthCodeData(code='$code', deviceId='$deviceId')"
    }
}
