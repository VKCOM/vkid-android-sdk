package com.vk.id.multibranding

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vk.id.multibranding.common.OAuthListWidgetCornersStyle

internal fun Modifier.clip(style: OAuthListWidgetCornersStyle): Modifier {
    return clip(RoundedCornerShape(style.radiusDp.dp))
}
