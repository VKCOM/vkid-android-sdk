package com.vk.id.group.subscription.common.fail

/**
 * Represents the failure cases for Group Subscription flow.
 *
 * @param description The fail description. Can be used for identifying the error. Should not be displayed to users.
 */
public sealed class VKIDGroupSubscriptionFail(
    public val description: String
) {

    /**
     * Represents the case when you passed access token from service account to the flow.
     */
    public class ServiceAccount : VKIDGroupSubscriptionFail(
        description = "User can't subscribe to group with service account"
    ) {

        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is ServiceAccount && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    /**
     * Represents an error when user is already a member of the group.
     */
    public class AlreadyGroupMember : VKIDGroupSubscriptionFail(
        description = "User can't subscribe to group he's already a member of"
    ) {

        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is AlreadyGroupMember && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    /**
     * Represents uncategorized errors, check [description] for more info.
     *
     * @param throwable The cause of the fail.
     */
    public class Other(
        public val throwable: Throwable
    ) : VKIDGroupSubscriptionFail(
        description = throwable.message.orEmpty()
    ) {
        /** @suppress */
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Other

            return description == other.description && throwable == other.throwable
        }

        /** @suppress */
        override fun hashCode(): Int {
            var result = description.hashCode()
            result = 31 * result + throwable.hashCode()
            return result
        }
    }

    /**
     * Represents flow cancellation by closing the sheet.
     */
    public class Close : VKIDGroupSubscriptionFail("User clicked on the close button") {

        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is Close && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    /**
     * Represents flow cancellation by clicking on the cancel button.
     */
    public class Cancel : VKIDGroupSubscriptionFail("User clicked on the cancel button") {

        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is Cancel && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }

    /**
     * Represents flow cancellation by sheet dismission.
     */
    public class Dismiss : VKIDGroupSubscriptionFail("User dismissed the sheet") {

        /** @suppress */
        override fun equals(other: Any?): Boolean {
            return other is Dismiss && description == other.description
        }

        /** @suppress */
        override fun hashCode(): Int {
            return description.hashCode()
        }
    }
}
