package com.vk.id.group.subscription.common.style

import androidx.compose.runtime.Immutable

/**
 * Group subscription sheet button's corners style.
 *
 * @since 2.5.0
 */
@Immutable
public sealed class GroupSubscriptionButtonsCornersStyle(
    /**
     * Corner radius in DPs.
     *
     * @since 2.5.0
     */
    public val radiusDp: Float
) {
    /**
     * The recommended style.
     *
     * @since 2.5.0
     */
    @Suppress("ConvertObjectToDataObject")
    public object Default : GroupSubscriptionButtonsCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * A style without rounding.
     *
     * @since 2.5.0
     */
    @Suppress("ConvertObjectToDataObject")
    public object None : GroupSubscriptionButtonsCornersStyle(NONE_RADIUS_DP)

    /**
     * Slightly rounded style.
     *
     * @since 2.5.0
     */
    @Suppress("ConvertObjectToDataObject")
    public object Rounded : GroupSubscriptionButtonsCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * Fully round style.
     *
     * @since 2.5.0
     */
    @Suppress("ConvertObjectToDataObject")
    public object Round : GroupSubscriptionButtonsCornersStyle(ROUND_RADIUS_DP)

    /**
     * A custom style with required corner radius.
     *
     * @param radiusDp Radius in DPs.
     *
     * @since 2.5.0
     */
    public class Custom(radiusDp: Float) : GroupSubscriptionButtonsCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8f
        private const val NONE_RADIUS_DP = 0f
        private const val ROUND_RADIUS_DP = 1000f
    }
}
