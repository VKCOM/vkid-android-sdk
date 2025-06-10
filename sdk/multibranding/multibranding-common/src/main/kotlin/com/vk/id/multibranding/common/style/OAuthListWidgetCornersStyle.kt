package com.vk.id.multibranding.common.style

import androidx.compose.runtime.Immutable

/**
 * OAuthListWidget's corners style.
 *
 * @since 1.0.0
 */
@Immutable
public sealed class OAuthListWidgetCornersStyle(
    /**
     * Corner radius in DPs.
     *
     * @since 1.0.0
     */
    public val radiusDp: Float
) {
    /**
     * The recommended style.
     *
     * @since 1.0.0
     */
    public object Default : OAuthListWidgetCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * A style without rounding.
     *
     * @since 1.0.0
     */
    public object None : OAuthListWidgetCornersStyle(NONE_RADIUS_DP)

    /**
     * Slightly rounded style.
     *
     * @since 1.0.0
     */
    public object Rounded : OAuthListWidgetCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * Fully round style.
     *
     * @since 1.0.0
     */
    public object Round : OAuthListWidgetCornersStyle(ROUND_RADIUS_DP)

    /**
     * A custom style with required corner radius.
     *
     * @param radiusDp Radius in DPs.
     *
     * @since 1.0.0
     */
    public class Custom(radiusDp: Float) : OAuthListWidgetCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8F
        private const val NONE_RADIUS_DP = 0F
        private const val ROUND_RADIUS_DP = 1000F
    }
}
