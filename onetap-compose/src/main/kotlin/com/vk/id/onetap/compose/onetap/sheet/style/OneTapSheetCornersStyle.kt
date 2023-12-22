package com.vk.id.onetap.compose.onetap.sheet.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Represents the corner styles for One Tap Sheet components.
 *
 * @param radiusDp The radius of the corners in density-independent pixels.
 */
public sealed class OneTapSheetCornersStyle(
    public val radiusDp: Float
) {
    /**
     * Default corner style with a 12 dp radius.
     */
    public object Default : OneTapSheetCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * Style with no rounding (square corners).
     */
    public object None : OneTapSheetCornersStyle(NONE_RADIUS_DP)

    /**
     * Rounded corner style with a 12 dp radius.
     */
    public object Rounded : OneTapSheetCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * Custom corner style with a specified radius.
     */
    public class Custom(radiusDp: Float) : OneTapSheetCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 12f
        private const val NONE_RADIUS_DP = 0f
    }
}

internal fun Modifier.clip(style: OneTapSheetCornersStyle): Modifier {
    return clip(RoundedCornerShape(style.radiusDp.dp))
}
