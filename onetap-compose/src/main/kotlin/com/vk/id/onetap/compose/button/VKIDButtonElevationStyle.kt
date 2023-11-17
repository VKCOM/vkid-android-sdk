package com.vk.id.onetap.compose.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.vk.id.multibranding.CornersStyle

public sealed class VKIDButtonElevationStyle(
    public val elevation: Int
) {
    public object Default : VKIDButtonElevationStyle(0)
    public class Custom(elevation: Int) : VKIDButtonElevationStyle(elevation)
}

internal fun Modifier.shadow(
    style: VKIDButtonElevationStyle,
    cornersStyle: CornersStyle
) = shadow(
    elevation = style.elevation.dp,
    shape = RoundedCornerShape(cornersStyle.radiusDp.dp),
)
