package com.vk.id.multibranding

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vk.id.multibranding.compose.R

public enum class BorderStyle {
    NONE,
    DARK,
    LIGHT,
}

public fun Modifier.border(
    style: BorderStyle,
    cornersStyle: CornersStyle,
): Modifier = composed {
    when (style) {
        BorderStyle.NONE -> this@border
        BorderStyle.DARK -> border(
            width = 1.dp,
            color = colorResource(id = R.color.vkid_black_alpha12),
            shape = RoundedCornerShape(size = cornersStyle.radiusDp.dp)
        )
        BorderStyle.LIGHT -> border(
            width = 1.dp,
            color = colorResource(id = R.color.vkid_white_alpha12),
            shape = RoundedCornerShape(size = cornersStyle.radiusDp.dp)
        )
    }
}
