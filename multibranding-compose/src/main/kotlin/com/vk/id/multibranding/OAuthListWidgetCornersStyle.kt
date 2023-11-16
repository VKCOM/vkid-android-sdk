package com.vk.id.multibranding

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

public sealed class OAuthListWidgetCornersStyle(
    public val radiusDp: Int
) {
    public object Default : OAuthListWidgetCornersStyle(ROUNDED_RADIUS_DP)
    public object None : OAuthListWidgetCornersStyle(NONE_RADIUS_DP)
    public object Rounded : OAuthListWidgetCornersStyle(ROUNDED_RADIUS_DP)
    public object Round : OAuthListWidgetCornersStyle(ROUND_RADIUS_DP)
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
