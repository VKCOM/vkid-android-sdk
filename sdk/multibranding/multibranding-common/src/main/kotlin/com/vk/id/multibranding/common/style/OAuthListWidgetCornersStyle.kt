package com.vk.id.multibranding.common.style

/**
 * OAuthListWidget's corners style.
 */
public sealed class OAuthListWidgetCornersStyle(
    /**
     * Corner radius in DPs.
     */
    public val radiusDp: Float
) {
    /**
     * The recommended style.
     */
    public object Default : OAuthListWidgetCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * A style without rounding.
     */
    public object None : OAuthListWidgetCornersStyle(NONE_RADIUS_DP)

    /**
     * Slightly rounded style.
     */
    public object Rounded : OAuthListWidgetCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * Fully round style.
     */
    public object Round : OAuthListWidgetCornersStyle(ROUND_RADIUS_DP)

    /**
     * A custom style with required corner radius.
     *
     * @param radiusDp Radius in DPs.
     */
    public class Custom(radiusDp: Float) : OAuthListWidgetCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8F
        private const val NONE_RADIUS_DP = 0F
        private const val ROUND_RADIUS_DP = 1000F
    }
}
