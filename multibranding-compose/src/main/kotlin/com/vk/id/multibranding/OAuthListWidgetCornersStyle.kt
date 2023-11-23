package com.vk.id.multibranding

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * [OAuthListWidget]'s corners style.
 */
public sealed class OAuthListWidgetCornersStyle(
    /**
     * Corner radius in DPs.
     */
    public val radiusDp: Int
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
     */
    public class Custom(radiusDp: Int) : OAuthListWidgetCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8
        private const val NONE_RADIUS_DP = 0
        private const val ROUND_RADIUS_DP = 1000
    }
}

internal fun Modifier.clip(style: OAuthListWidgetCornersStyle): Modifier {
    return clip(RoundedCornerShape(style.radiusDp.dp))
}
