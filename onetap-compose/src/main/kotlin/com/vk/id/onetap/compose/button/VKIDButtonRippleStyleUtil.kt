package com.vk.id.onetap.compose.button

import androidx.compose.ui.graphics.Color
import com.vk.id.onetap.common.button.VKIDButtonRippleStyle

internal fun VKIDButtonRippleStyle.asColor() = when (this) {
    VKIDButtonRippleStyle.DARK -> Color.Black
    VKIDButtonRippleStyle.LIGHT -> Color.White
}
