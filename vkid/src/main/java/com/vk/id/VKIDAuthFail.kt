package com.vk.id

public sealed class VKIDAuthFail(
    public val description: String
) {
    public class Canceled(
        description: String
    ) : VKIDAuthFail(description) {
        override fun equals(other: Any?): Boolean {
            return other is Canceled && description == other.description
        }

        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    public class FailedApiCall(
        description: String,
        public val throwable: Throwable
    ) : VKIDAuthFail(description) {
        override fun equals(other: Any?): Boolean {
            return other is FailedApiCall && description == other.description && throwable == other.throwable
        }

        override fun hashCode(): Int {
            var result = description.hashCode()
            result = 31 * result + throwable.hashCode()
            return result
        }
    }

    public class FailedOAuthState(description: String) : VKIDAuthFail(description) {
        override fun equals(other: Any?): Boolean {
            return other is FailedOAuthState && description == other.description
        }

        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    public class FailedRedirectActivity(
        description: String,
        public val throwable: Throwable?
    ) : VKIDAuthFail(description) {
        override fun equals(other: Any?): Boolean {
            return other is FailedRedirectActivity && description == other.description && throwable == other.throwable
        }

        override fun hashCode(): Int {
            var result = description.hashCode()
            result = 31 * result + throwable.hashCode()
            return result
        }
    }

    public class NoBrowserAvailable(
        description: String,
        public val throwable: Throwable?
    ) : VKIDAuthFail(description) {
        override fun equals(other: Any?): Boolean {
            return other is NoBrowserAvailable && description == other.description && throwable == other.throwable
        }

        override fun hashCode(): Int {
            var result = description.hashCode()
            result = 31 * result + throwable.hashCode()
            return result
        }
    }
}
