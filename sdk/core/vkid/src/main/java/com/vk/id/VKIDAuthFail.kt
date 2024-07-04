package com.vk.id

/**
 * Represents the failure cases for VK ID authentication.
 */
public sealed class VKIDAuthFail(
    /**
     * Text description of the failure.
     */
    public val description: String
) {
    /**
     * Authentication process was canceled.
     *
     * @param description Description of the cancellation.
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
     * @param throwable The exception thrown during the API call.
     */
    public class FailedApiCall(
        description: String,
        public val throwable: Throwable?
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
     */
    public class FailedOAuth(description: String) : VKIDAuthFail(description)

    /**
     * Represents a failure due to an invalid OAuth state.
     *
     * @param description Description of the OAuth state failure.
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
     * @param throwable Optional exception thrown during the redirect activity process.
     */
    public class FailedRedirectActivity(
        description: String,
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
     * @param throwable Optional exception related to the absence of the browser.
     */
    public class NoBrowserAvailable(
        description: String,
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
