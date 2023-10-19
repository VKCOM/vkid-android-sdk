package com.vk.id.onetap.compose.button

import androidx.compose.ui.graphics.Color

public enum class VKIDButtonRippleStyle {
    DARK,
    LIGHT,
}

internal fun VKIDButtonRippleStyle.asColor() = when (this) {
    VKIDButtonRippleStyle.DARK -> Color.Black
    VKIDButtonRippleStyle.LIGHT -> Color.White
}
