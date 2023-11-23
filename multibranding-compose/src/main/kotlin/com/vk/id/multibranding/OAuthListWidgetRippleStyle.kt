package com.vk.id.multibranding

import androidx.compose.ui.graphics.Color

/**
 * [OAuthListWidget]'s ripple style.
 */
public enum class OAuthListWidgetRippleStyle {
    /**
     * Dark version, should be used for the light layout.
     */
    DARK,

    /**
     * Light version, should be used for the dark layout.
     */
    LIGHT,
}

internal fun OAuthListWidgetRippleStyle.asColor(): Color = when (this) {
    OAuthListWidgetRippleStyle.DARK -> Color.Black
    OAuthListWidgetRippleStyle.LIGHT -> Color.White
}
