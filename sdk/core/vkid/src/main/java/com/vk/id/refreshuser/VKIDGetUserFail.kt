package com.vk.id.refreshuser

/**
 * Represents the failure cases for VK ID user refreshing.
 *
 * @since 2.0.0-alpha
 */
public sealed class VKIDGetUserFail(
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
    ) : VKIDGetUserFail(description) {
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

        /** @suppress */
        override fun toString(): String {
            return "FailedApiCall(description=$description&throwable=$throwable)"
        }
    }

    /**
     * Represents a failure due to an expiration of id token which is used to fetch user data.
     *
     * @param description Description of the token expiration error.
     *
     * @since 2.0.0-alpha
     */
    public class IdTokenTokenExpired(
        description: String
    ) : VKIDGetUserFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is IdTokenTokenExpired && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    /**
     * Represents a failure due to lack of auth before fetching user data.
     *
     * @param description Description of the failure.
     *
     * @since 2.0.0-alpha
     */
    public class NotAuthenticated(
        description: String
    ) : VKIDGetUserFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is VKIDGetUserFail && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }
}
