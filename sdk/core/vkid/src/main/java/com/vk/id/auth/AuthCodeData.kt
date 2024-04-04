package com.vk.id.auth

/**
 * The data with result of the first step of auth.
 *
 * @property code Auth code.
 */
public class AuthCodeData(
    public val code: String,
) {

    /** @suppress */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AuthCodeData

        return code == other.code
    }

    /** @suppress */
    override fun hashCode(): Int {
        return code.hashCode()
    }
}
