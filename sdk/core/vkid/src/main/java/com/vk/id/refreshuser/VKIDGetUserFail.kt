package com.vk.id.refreshuser

/**
 * Represents the failure cases for VK ID user refreshing.
 */
public sealed class VKIDGetUserFail(
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
    }

    /**
     * Represents a failure due to an expiration of id token which is used to fetch user data.
     *
     * @param description Description of the token expiration error.
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
