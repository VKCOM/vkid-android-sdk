package com.vk.id.multibranding

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

public enum class OAuthListWidgetTextStyle {
    LIGHT,
    DARK
}

@Composable
internal fun OAuthListWidgetTextStyle.asColorResource() = when (this) {
    OAuthListWidgetTextStyle.DARK -> Color.Black
    OAuthListWidgetTextStyle.LIGHT -> Color.White
}
