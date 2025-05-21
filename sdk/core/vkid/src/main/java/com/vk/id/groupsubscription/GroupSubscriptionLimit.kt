package com.vk.id.groupsubscription

/**
 * Represents limits for group subscription feature.
 *
 * For any window of [periodInDays] days there will be no more
 * than [maxSubscriptionsToShow] displays of group subscription sheet for the same user.
 *
 * @param maxSubscriptionsToShow Max number of displays of group subscription sheet in any [periodInDays].
 * @param periodInDays Window for which limit in [maxSubscriptionsToShow] will be applied.
 *
 * @since 2.5.0
 */
public class GroupSubscriptionLimit(
    /**
     * Max number of displays of group subscription sheet in any [periodInDays].
     *
     * @since 2.5.0
     */
    public val maxSubscriptionsToShow: Int = 2,
    /**
     * Window for which limit in [maxSubscriptionsToShow] will be applied.
     *
     * @since 2.5.0
     */
    public val periodInDays: Int = 30,
)
