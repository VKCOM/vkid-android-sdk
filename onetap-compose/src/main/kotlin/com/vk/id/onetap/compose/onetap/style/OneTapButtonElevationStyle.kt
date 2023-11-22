package com.vk.id.onetap.compose.onetap.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

public sealed class OneTapButtonElevationStyle(
    public val elevation: Int
) {
    public object Default : OneTapButtonElevationStyle(0)
    public class Custom(elevation: Int) : OneTapButtonElevationStyle(elevation)
}

internal fun Modifier.shadow(
    style: OneTapButtonElevationStyle,
    cornersStyle: OneTapButtonCornersStyle
) = shadow(
    elevation = style.elevation.dp,
    shape = RoundedCornerShape(cornersStyle.radiusDp.dp),
)
