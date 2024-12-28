package com.vk.id.group.subscription.common

public sealed class VKIDGroupSubscriptionFail(
    public val description: String
) {

    public class Cancel : VKIDGroupSubscriptionFail(
        description = "User cancelled the flow"
    ) {

        override fun equals(other: Any?): Boolean {
            return other is Cancel && description == other.description
        }

        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    public class ServiceAccount : VKIDGroupSubscriptionFail(
        description = "User can't subscribe to group with service account"
    ) {

        override fun equals(other: Any?): Boolean {
            return other is ServiceAccount && description == other.description
        }

        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    public class AlreadyGroupMember : VKIDGroupSubscriptionFail(
        description = "User can't subscribe to group he's already a member of"
    ) {

        override fun equals(other: Any?): Boolean {
            return other is AlreadyGroupMember && description == other.description
        }

        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    public class Other(
        public val throwable: Throwable
    ) : VKIDGroupSubscriptionFail(
        description = throwable.message.orEmpty()
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Other

            return description == other.description && throwable == other.throwable
        }

        override fun hashCode(): Int {
            var result = description.hashCode()
            result = 31 * result + throwable.hashCode()
            return result
        }
    }
}
