package com.vk.id.multibranding

import androidx.compose.ui.graphics.Color

public enum class RippleStyle {
    DARK,
    LIGHT,
}

public fun RippleStyle.asColor(): Color = when (this) {
    RippleStyle.DARK -> Color.Black
    RippleStyle.LIGHT -> Color.White
}
