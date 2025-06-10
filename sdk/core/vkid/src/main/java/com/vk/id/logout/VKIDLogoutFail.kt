package com.vk.id.logout

/**
 * Represents the failure cases for VK ID logout.
 *
 * @since 2.0.0-alpha
 */
public sealed class VKIDLogoutFail(
    /**
     * Text description of the failure.
     *
     * @since 2.0.0-alpha
     */
    public val description: String
) {
    /**
     * Represents a failure due to an VK ID API call error.
     *
     * @param description Description of the API call failure.
     * @param throwable The exception thrown during the API call.
     *
     * @since 2.0.0-alpha
     */
    public class FailedApiCall(
        description: String,
        /**
         * The exception thrown during the API call.
         *
         * @since 2.0.0-alpha
         */
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
     *
     * @since 2.0.0-alpha
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
     *
     * @since 2.0.0-alpha
     */
    public class NotAuthenticated(
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
