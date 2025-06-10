package com.vk.id.refresh

/**
 * Represents the failure cases for VK ID token refreshing.
 *
 * @since 2.0.0-alpha
 */
public sealed class VKIDRefreshTokenFail(
    /**
     * Text description of the failure.
     *
     * @since 2.0.0-alpha
     */
    public val description: String
) {
    /**
     * Represents a failure due to an invalid OAuth state.
     *
     * @param description Description of the OAuth state failure.
     *
     * @since 2.0.0-alpha
     */
    public class FailedOAuthState(description: String) : VKIDRefreshTokenFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is FailedOAuthState && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

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

        /** @suppress */
        override fun toString(): String {
            return "FailedApiCall(description=$description,throwable=$throwable)"
        }
    }

    /**
     * Represents a failure due to an expiration of refresh token.
     *
     * @param description Description of the token refreshing failure.
     *
     * @since 2.0.0-alpha
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
     *
     * @since 2.0.0-alpha
     */
    public class NotAuthenticated(
        description: String
    ) : VKIDRefreshTokenFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is NotAuthenticated && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }
}
