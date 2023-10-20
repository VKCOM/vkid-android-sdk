package com.vk.id.onetap.compose.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.common.button.VKIDButtonCornersStyle

internal fun Modifier.clip(style: VKIDButtonCornersStyle): Modifier {
    return clip(RoundedCornerShape(style.radiusDp.dp))
}
