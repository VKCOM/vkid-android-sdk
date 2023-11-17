package com.vk.id.multibranding

import androidx.compose.ui.graphics.Color

public enum class WidgetRippleStyle {
    DARK,
    LIGHT,
}

public fun WidgetRippleStyle.asColor(): Color = when (this) {
    WidgetRippleStyle.DARK -> Color.Black
    WidgetRippleStyle.LIGHT -> Color.White
}
