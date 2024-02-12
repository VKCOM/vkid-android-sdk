package com.vk.id.refresh

/**
 * Represents the failure cases for VK ID token refreshing.
 */
public sealed class VKIDRefreshTokenFail(
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
    ) : VKIDRefreshTokenFail(description) {
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
     * Represents a failure due to an expiration of refresh token.
     *
     * @param description Description of the token refreshing failure.
     */
    public class RefreshTokenExpired(
        description: String
    ) : VKIDRefreshTokenFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is RefreshTokenExpired && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    /**
     * Represents a failure due to lack of auth before token refreshing.
     *
     * @param description Description of the failure.
     */
    public class Unauthorized(
        description: String
    ) : VKIDRefreshTokenFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is RefreshTokenExpired && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }
}
