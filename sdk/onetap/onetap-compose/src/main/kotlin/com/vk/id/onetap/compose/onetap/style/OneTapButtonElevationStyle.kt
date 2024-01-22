package com.vk.id.onetap.compose.onetap.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle

internal fun Modifier.shadow(
    style: OneTapButtonElevationStyle,
    cornersStyle: OneTapButtonCornersStyle
) = shadow(
    elevation = style.elevationDp.dp,
    shape = RoundedCornerShape(cornersStyle.radiusDp.dp),
)
