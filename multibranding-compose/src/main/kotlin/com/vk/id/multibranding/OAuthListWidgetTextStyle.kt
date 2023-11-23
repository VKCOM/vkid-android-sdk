package com.vk.id.multibranding

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * [OAuthListWidget]'s text style.
 */
public enum class OAuthListWidgetTextStyle {
    /**
     * Light version, should be used for the dark layout.
     */
    LIGHT,

    /**
     * Dark version, should be used for the light layout.
     */
    DARK
}

@Composable
internal fun OAuthListWidgetTextStyle.asColorResource() = when (this) {
    OAuthListWidgetTextStyle.DARK -> Color.Black
    OAuthListWidgetTextStyle.LIGHT -> Color.White
}
