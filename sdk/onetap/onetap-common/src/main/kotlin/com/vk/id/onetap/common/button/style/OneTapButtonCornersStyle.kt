package com.vk.id.onetap.common.button.style

import androidx.compose.runtime.Immutable

/**
 * OneTap's corners style.
 *
 * @since 1.0.0
 */
@Immutable
public sealed class OneTapButtonCornersStyle(
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
    public object Default : OneTapButtonCornersStyle(DEFAULT_RADIUS_DP)

    /**
     * A style without rounding.
     *
     * @since 1.0.0
     */
    public object None : OneTapButtonCornersStyle(NONE_RADIUS_DP)

    /**
     * Slightly rounded style.
     *
     * @since 1.0.0
     */
    public object Rounded : OneTapButtonCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * Fully round style.
     *
     * @since 1.0.0
     */
    public object Round : OneTapButtonCornersStyle(ROUND_RADIUS_DP)

    /**
     * A custom style with required corner radius.
     *
     * @param radiusDp Radius in DPs.
     *
     * @since 1.0.0
     */
    public class Custom(radiusDp: Float) : OneTapButtonCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8f
        private const val DEFAULT_RADIUS_DP = 12f
        private const val NONE_RADIUS_DP = 0f
        private const val ROUND_RADIUS_DP = 1000f
    }
}
