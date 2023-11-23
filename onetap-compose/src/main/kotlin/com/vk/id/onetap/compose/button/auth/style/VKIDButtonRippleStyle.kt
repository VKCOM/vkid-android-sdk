package com.vk.id.onetap.compose.button.auth.style

import androidx.compose.ui.graphics.Color

internal enum class VKIDButtonRippleStyle {
    DARK,
    LIGHT,
}

internal fun VKIDButtonRippleStyle.asColor() = when (this) {
    VKIDButtonRippleStyle.DARK -> Color.Black
    VKIDButtonRippleStyle.LIGHT -> Color.White
}
