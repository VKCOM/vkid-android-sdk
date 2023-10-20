package com.vk.id.onetap.compose.button

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.compose.R

internal enum class VKIDButtonBorderStyle {
    NONE,
    DARK,
    LIGHT,
}

internal fun Modifier.border(
    style: VKIDButtonBorderStyle,
    cornersStyle: VKIDButtonCornersStyle,
) = composed {
    when (style) {
        VKIDButtonBorderStyle.NONE -> this@border
        VKIDButtonBorderStyle.DARK -> border(
            width = 1.dp,
            color = colorResource(id = R.color.vkid_black_alpha12),
            shape = RoundedCornerShape(size = cornersStyle.radiusDp.dp)
        )
        VKIDButtonBorderStyle.LIGHT -> border(
            width = 1.dp,
            color = colorResource(id = R.color.vkid_white_alpha12),
            shape = RoundedCornerShape(size = cornersStyle.radiusDp.dp)
        )
    }
}
