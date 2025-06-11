package com.vk.id.group.subscription.common.style

import androidx.compose.runtime.Immutable

/**
 * Represents the corner styles for Group Subscription Sheet components.
 *
 * @param radiusDp The radius of the corners in density-independent pixels.
 *
 * @since 2.5.0
 */
@Immutable
@Suppress("ConvertObjectToDataObject")
public sealed class GroupSubscriptionSheetCornersStyle(
    /**
     * The radius of the corners in density-independent pixels.
     *
     * @since 2.5.0
     */
    public val radiusDp: Float
) {
    /**
     * Default corner style with a 12 dp radius.
     *
     * @since 2.5.0
     */
    public object Default : GroupSubscriptionSheetCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * Style with no rounding (square corners).
     *
     * @since 2.5.0
     */
    public object None : GroupSubscriptionSheetCornersStyle(NONE_RADIUS_DP)

    /**
     * Rounded corner style with a 12 dp radius.
     *
     * @since 2.5.0
     */
    public object Rounded : GroupSubscriptionSheetCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * Custom corner style with a specified radius.
     *
     * @since 2.5.0
     */
    public class Custom(radiusDp: Float) : GroupSubscriptionSheetCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 12f
        private const val NONE_RADIUS_DP = 0f
    }
}
