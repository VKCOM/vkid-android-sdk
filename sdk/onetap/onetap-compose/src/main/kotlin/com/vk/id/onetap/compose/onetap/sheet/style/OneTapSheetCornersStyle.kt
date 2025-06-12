package com.vk.id.onetap.compose.onetap.sheet.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Represents the corner styles for One Tap Sheet components.
 *
 * @param radiusDp The radius of the corners in density-independent pixels.
 *
 * @since 0.0.1
 */
@Immutable
public sealed class OneTapSheetCornersStyle(
    /**
     * The radius of the corners in density-independent pixels.
     *
     * @since 0.0.1
     */
    public val radiusDp: Float
) {
    /**
     * Default corner style with a 24 dp radius.
     *
     * @since 0.0.1
     */
    @Suppress("ConvertObjectToDataObject")
    public object Default : OneTapSheetCornersStyle(DEFAULT_RADIUS_DP)

    /**
     * Style with no rounding (square corners).
     *
     * @since 0.0.1
     */
    @Suppress("ConvertObjectToDataObject")
    public object None : OneTapSheetCornersStyle(NONE_RADIUS_DP)

    /**
     * Rounded corner style with a 12 dp radius.
     *
     * @since 0.0.1
     */
    @Suppress("ConvertObjectToDataObject")
    public object Rounded : OneTapSheetCornersStyle(ROUNDED_RADIUS_DP)

    /**
     * Custom corner style with a specified radius.
     *
     * @since 0.0.1
     */
    public class Custom(radiusDp: Float) : OneTapSheetCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 12f
        private const val DEFAULT_RADIUS_DP = 24f
        private const val NONE_RADIUS_DP = 0f
    }
}

internal fun Modifier.clip(style: OneTapSheetCornersStyle): Modifier {
    return clip(RoundedCornerShape(style.radiusDp.dp))
}
