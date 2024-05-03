package com.vk.id.multibranding

import androidx.compose.ui.graphics.Color
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.common.style.InternalVKIDOAuthListWidgetRippleStyle

@OptIn(InternalVKIDApi::class)
internal fun InternalVKIDOAuthListWidgetRippleStyle.asColor(): Color = when (this) {
    InternalVKIDOAuthListWidgetRippleStyle.DARK -> Color.Black
    InternalVKIDOAuthListWidgetRippleStyle.LIGHT -> Color.White
}
