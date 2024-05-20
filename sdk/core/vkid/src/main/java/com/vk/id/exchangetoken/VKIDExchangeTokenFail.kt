package com.vk.id.exchangetoken

/**
 * Represents the failure cases for VK ID token exchange.
 */
public sealed class VKIDExchangeTokenFail(
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
    ) : VKIDExchangeTokenFail(description) {
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
            return "FailedApiCall(description=$description, throwable=$throwable)"
        }
    }

    /**
     * Represents a failure due to an invalid OAuth state.
     *
     * @param description Description of the OAuth state failure.
     */
    public class FailedOAuthState(description: String) : VKIDExchangeTokenFail(description) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is FailedOAuthState && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }
}
