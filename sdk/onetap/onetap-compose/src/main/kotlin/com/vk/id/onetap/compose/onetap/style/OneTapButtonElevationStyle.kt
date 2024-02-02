@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.VKIDButtonStyle

internal fun Modifier.shadow(
    style: VKIDButtonStyle,
): Modifier {
    val elevation = if (style !is VKIDButtonStyle.TransparentLight && style !is VKIDButtonStyle.TransparentDark) {
        style.elevationStyle.elevationDp
    } else {
        0f
    }
    return shadow(
        elevation = elevation.dp,
        shape = RoundedCornerShape(style.cornersStyle.radiusDp.dp),
    )
}
