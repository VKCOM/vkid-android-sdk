package com.vk.id.onetap.compose.onetap.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle

internal fun Modifier.clip(style: OneTapButtonCornersStyle): Modifier {
    return clip(RoundedCornerShape(style.radiusDp.dp))
}
