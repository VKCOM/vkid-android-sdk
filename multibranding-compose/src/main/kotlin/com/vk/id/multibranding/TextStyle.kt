package com.vk.id.multibranding

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

public enum class TextStyle {
    LIGHT,
    DARK
}

@Composable
public fun TextStyle.asColorResource(): Color = when (this) {
    TextStyle.DARK -> Color.Black
    TextStyle.LIGHT -> Color.White
}
