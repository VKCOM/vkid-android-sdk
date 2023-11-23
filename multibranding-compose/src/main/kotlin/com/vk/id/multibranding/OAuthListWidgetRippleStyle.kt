package com.vk.id.multibranding

import androidx.compose.ui.graphics.Color
import com.vk.id.multibranding.common.OAuthListWidgetRippleStyle

internal fun OAuthListWidgetRippleStyle.asColor(): Color = when (this) {
    OAuthListWidgetRippleStyle.DARK -> Color.Black
    OAuthListWidgetRippleStyle.LIGHT -> Color.White
}
