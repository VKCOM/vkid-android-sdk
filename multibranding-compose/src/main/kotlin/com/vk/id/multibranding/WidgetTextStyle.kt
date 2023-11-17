package com.vk.id.multibranding

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

public enum class WidgetTextStyle {
    LIGHT,
    DARK
}

@Composable
public fun WidgetTextStyle.asColorResource(): Color = when (this) {
    WidgetTextStyle.DARK -> Color.Black
    WidgetTextStyle.LIGHT -> Color.White
}
