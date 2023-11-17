package com.vk.id.multibranding

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

public sealed class CornersStyle(
    public val radiusDp: Int
) {
    public object Default : CornersStyle(ROUNDED_RADIUS_DP)
    public object None : CornersStyle(NONE_RADIUS_DP)
    public object Rounded : CornersStyle(ROUNDED_RADIUS_DP)
    public object Round : CornersStyle(ROUND_RADIUS_DP)
    public class Custom(radiusDp: Int) : CornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8
        private const val NONE_RADIUS_DP = 0
        private const val ROUND_RADIUS_DP = 1000
    }
}

public fun Modifier.clip(style: CornersStyle): Modifier {
    return clip(RoundedCornerShape(style.radiusDp.dp))
}
