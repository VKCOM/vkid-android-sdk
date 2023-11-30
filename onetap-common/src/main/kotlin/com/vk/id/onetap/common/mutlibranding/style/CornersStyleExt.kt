package com.vk.id.onetap.common.mutlibranding.style

import com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle

internal fun OneTapButtonCornersStyle.toOAuthListWidgetStyle() = when (this) {
    is OneTapButtonCornersStyle.Custom -> OAuthListWidgetCornersStyle.Custom(radiusDp)
    is OneTapButtonCornersStyle.Default -> OAuthListWidgetCornersStyle.Default
    is OneTapButtonCornersStyle.None -> OAuthListWidgetCornersStyle.None
    is OneTapButtonCornersStyle.Round -> OAuthListWidgetCornersStyle.Round
    is OneTapButtonCornersStyle.Rounded -> OAuthListWidgetCornersStyle.Rounded
}
