package com.vk.id.multibranding

import androidx.compose.ui.graphics.Color
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.common.style.VKIDOAuthListWidgetRippleStyle

@OptIn(InternalVKIDApi::class)
internal fun VKIDOAuthListWidgetRippleStyle.asColor(): Color = when (this) {
    VKIDOAuthListWidgetRippleStyle.DARK -> Color.Black
    VKIDOAuthListWidgetRippleStyle.LIGHT -> Color.White
}
