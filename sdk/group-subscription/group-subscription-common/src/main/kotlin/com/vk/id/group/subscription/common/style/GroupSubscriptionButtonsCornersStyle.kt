package com.vk.id.group.subscription.common.style

import androidx.compose.runtime.Immutable

/**
 * Group subscription sheet button's corners style.
 */
@Immutable
public sealed class GroupSubscriptionButtonsCornersStyle(
    /**
     * Corner radius in DPs.
     */
    public val radiusDp: Float
) {
    /**
     * The recommended style.
     */
    public object Default : GroupSubscriptionButtonsCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * A style without rounding.
     */
    public object None : GroupSubscriptionButtonsCornersStyle(NONE_RADIUS_DP)

    /**
     * Slightly rounded style.
     */
    public object Rounded : GroupSubscriptionButtonsCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * Fully round style.
     */
    public object Round : GroupSubscriptionButtonsCornersStyle(ROUND_RADIUS_DP)

    /**
     * A custom style with required corner radius.
     *
     * @param radiusDp Radius in DPs.
     */
    public class Custom(radiusDp: Float) : GroupSubscriptionButtonsCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8f
        private const val NONE_RADIUS_DP = 0f
        private const val ROUND_RADIUS_DP = 1000f
    }
}
