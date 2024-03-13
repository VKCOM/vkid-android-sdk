package com.vk.id.logout

/**
 * Represents the failure cases for VK ID logout.
 */
public sealed class VKIDLogoutFail(
    /**
     * Text description of the failure.
     */
    public val description: String
) {
    /**
     * Represents a failure due to an VK ID API call error.
     *
     * @param description Description of the API call failure.
     * @param throwable The exception thrown during the API call.
     */
    public class FailedApiCall(
        description: String,
        public val throwable: Throwable
    ) : VKIDLogoutFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is FailedApiCall && description == other.description && throwable == other.throwable
        }

        /** @suppress */
        override fun hashCode(): Int {
            var result = description.hashCode()
            result = 31 * result + throwable.hashCode()
            return result
        }
    }

    /**
     * Represents a failure due to an expiration of access token which is used to logout.
     * In that case logging out in unnecessary.
     *
     * @param description Description of the token expiration error.
     */
    public class AccessTokenTokenExpired(
        description: String
    ) : VKIDLogoutFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is AccessTokenTokenExpired && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    /**
     * Represents a failure due to lack of auth before logging out.
     * In that case logging out in unnecessary.
     *
     * @param description Description of the failure.
     */
    public class Unauthorized(
        description: String
    ) : VKIDLogoutFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is VKIDLogoutFail && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }
}
