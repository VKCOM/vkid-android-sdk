package com.vk.id

/**
 * Represents the failure cases for VK ID authentication.
 *
 * @since 1.0.0
 */
public sealed class VKIDAuthFail(
    /**
     * Text description of the failure.
     *
     * @since 1.0.0
     */
    public val description: String
) {
    /**
     * Authentication process was canceled.
     *
     * @param description Description of the cancellation.
     *
     * @since 1.0.0
     */
    public class Canceled(
        description: String
    ) : VKIDAuthFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is Canceled && description == other.description
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
     *
     * @since 1.0.0
     */
    public class FailedApiCall(
        description: String,
        /**
         * The exception thrown during the API call.
         *
         * @since 1.0.0
         */
        public val throwable: Throwable
    ) : VKIDAuthFail(description) {
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
     * Represents a failure in the OAuth authentication process.
     *
     * @param description Description of the OAuth failure.
     *
     * @since 1.0.0
     */
    public class FailedOAuth(description: String) : VKIDAuthFail(description)

    /**
     * Represents a failure due to an invalid OAuth state.
     *
     * @param description Description of the OAuth state failure.
     *
     * @since 1.0.0
     */
    public class FailedOAuthState(description: String) : VKIDAuthFail(description) {
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
     * Represents a failure due to an issue with the redirect activity.
     *
     * @param description Description of the redirect activity failure.
     *
     * @since 1.0.0
     */
    public class FailedRedirectActivity(
        description: String,
        /**
         * Optional exception thrown during the redirect activity process.
         *
         * @since 1.0.0
         */
        public val throwable: Throwable?
    ) : VKIDAuthFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is FailedRedirectActivity && description == other.description && throwable == other.throwable
        }

        /** @suppress */
        override fun hashCode(): Int {
            var result = description.hashCode()
            result = 31 * result + throwable.hashCode()
            return result
        }
    }

    /**
     * Represents a failure due to the absence of a suitable browser.
     *
     * @param description Description of the failure due to no available browser.
     *
     * @since 1.0.0
     */
    public class NoBrowserAvailable(
        description: String,
        /**
         * Optional exception related to the absence of the browser.
         *
         * @since 1.0.0
         */
        public val throwable: Throwable?
    ) : VKIDAuthFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is NoBrowserAvailable && description == other.description && throwable == other.throwable
        }

        /** @suppress */
        override fun hashCode(): Int {
            var result = description.hashCode()
            result = 31 * result + throwable.hashCode()
            return result
        }
    }
}
