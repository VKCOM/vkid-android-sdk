package com.vk.id.auth

/**
 * The data with result of the first step of auth.
 *
 * @since 2.0.0-alpha
 */
public class AuthCodeData @JvmOverloads constructor(
    /**
     * Auth code.
     *
     * @since 2.0.0-alpha
     */
    public val code: String,
    /**
     * Device id to be passed to /auth when exchanging [code] for token.
     *
     * @since 2.0.1
     */
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
