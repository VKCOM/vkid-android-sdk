package com.vk.id.onetap.compose.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.vk.id.multibranding.WidgetCornersStyle

public sealed class ElevationStyle(
    public val elevation: Int
) {
    public object Default : ElevationStyle(0)
    public class Custom(elevation: Int) : ElevationStyle(elevation)
}

internal fun Modifier.shadow(
    style: ElevationStyle,
    cornersStyle: WidgetCornersStyle
) = shadow(
    elevation = style.elevation.dp,
    shape = RoundedCornerShape(cornersStyle.radiusDp.dp),
)
