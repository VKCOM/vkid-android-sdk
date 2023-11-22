package com.vk.id.onetap.compose.onetap.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

public sealed class OneTapButtonCornersStyle(
    public val radiusDp: Int
) {
    public object Default : OneTapButtonCornersStyle(ROUNDED_RADIUS_DP)
    public object None : OneTapButtonCornersStyle(NONE_RADIUS_DP)
    public object Rounded : OneTapButtonCornersStyle(ROUNDED_RADIUS_DP)
    public object Round : OneTapButtonCornersStyle(ROUND_RADIUS_DP)
    public class Custom(radiusDp: Int) : OneTapButtonCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8
        private const val NONE_RADIUS_DP = 0
        private const val ROUND_RADIUS_DP = 1000
    }
}

internal fun Modifier.clip(style: OneTapButtonCornersStyle): Modifier {
    return clip(RoundedCornerShape(style.radiusDp.dp))
}
