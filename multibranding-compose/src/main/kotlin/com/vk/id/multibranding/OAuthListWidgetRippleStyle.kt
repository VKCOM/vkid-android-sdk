package com.vk.id.multibranding

import androidx.compose.ui.graphics.Color
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.multibranding.common.style.OAuthListWidgetRippleStyle

@OptIn(InternalVKIDApi::class)
internal fun OAuthListWidgetRippleStyle.asColor(): Color = when (this) {
    OAuthListWidgetRippleStyle.DARK -> Color.Black
    OAuthListWidgetRippleStyle.LIGHT -> Color.White
}
