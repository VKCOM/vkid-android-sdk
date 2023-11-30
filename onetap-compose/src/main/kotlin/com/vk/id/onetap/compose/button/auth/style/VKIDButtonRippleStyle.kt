package com.vk.id.onetap.compose.button.auth.style

import androidx.compose.ui.graphics.Color
import com.vk.id.onetap.common.auth.style.VKIDButtonRippleStyle

internal fun VKIDButtonRippleStyle.asColor() = when (this) {
    VKIDButtonRippleStyle.DARK -> Color.Black
    VKIDButtonRippleStyle.LIGHT -> Color.White
}
