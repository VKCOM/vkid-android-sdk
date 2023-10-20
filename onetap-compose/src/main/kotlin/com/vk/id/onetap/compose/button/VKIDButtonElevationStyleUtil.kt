package com.vk.id.onetap.compose.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.common.button.VKIDButtonCornersStyle
import com.vk.id.onetap.common.button.VKIDButtonElevationStyle

internal fun Modifier.shadow(
    style: VKIDButtonElevationStyle,
    cornersStyle: VKIDButtonCornersStyle
) = shadow(
    elevation = style.elevation.dp,
    shape = RoundedCornerShape(cornersStyle.radiusDp.dp),
)
