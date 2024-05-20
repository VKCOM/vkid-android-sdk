@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.InternalVKIDButtonStyle

internal fun Modifier.shadow(
    style: InternalVKIDButtonStyle,
): Modifier {
    val elevation = if (style !is InternalVKIDButtonStyle.TransparentLight && style !is InternalVKIDButtonStyle.TransparentDark) {
        style.elevationStyle.elevationDp
    } else {
        0f
    }
    return shadow(
        elevation = elevation.dp,
        shape = RoundedCornerShape(style.cornersStyle.radiusDp.dp),
    )
}
