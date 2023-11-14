package com.vk.id.multibranding

import androidx.compose.ui.graphics.Color


// TODO ui kit?
public enum class OAuthListWidgetRippleStyle {
    DARK,
    LIGHT,
}

internal fun OAuthListWidgetRippleStyle.asColor() = when (this) {
    OAuthListWidgetRippleStyle.DARK -> Color.Black
    OAuthListWidgetRippleStyle.LIGHT -> Color.White
}
