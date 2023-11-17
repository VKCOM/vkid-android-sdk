package com.vk.id.multibranding

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vk.id.multibranding.compose.R

public enum class WidgetBorderStyle {
    NONE,
    DARK,
    LIGHT,
}

public fun Modifier.border(
    style: WidgetBorderStyle,
    cornersStyle: WidgetCornersStyle,
): Modifier = composed {
    when (style) {
        WidgetBorderStyle.NONE -> this@border
        WidgetBorderStyle.DARK -> border(
            width = 1.dp,
            color = colorResource(id = R.color.vkid_black_alpha12),
            shape = RoundedCornerShape(size = cornersStyle.radiusDp.dp)
        )
        WidgetBorderStyle.LIGHT -> border(
            width = 1.dp,
            color = colorResource(id = R.color.vkid_white_alpha12),
            shape = RoundedCornerShape(size = cornersStyle.radiusDp.dp)
        )
    }
}
