package com.vk.id.onetap.common.mutlibranding.style

import com.vk.id.multibranding.common.style.OAuthListWidgetSizeStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle

@Suppress("CyclomaticComplexMethod")
internal fun OneTapButtonSizeStyle.toOAuthListWidgetStyle() = when (this) {
    OneTapButtonSizeStyle.DEFAULT -> OAuthListWidgetSizeStyle.DEFAULT
    OneTapButtonSizeStyle.SMALL_32 -> OAuthListWidgetSizeStyle.SMALL_32
    OneTapButtonSizeStyle.SMALL_34 -> OAuthListWidgetSizeStyle.SMALL_34
    OneTapButtonSizeStyle.SMALL_36 -> OAuthListWidgetSizeStyle.SMALL_36
    OneTapButtonSizeStyle.SMALL_38 -> OAuthListWidgetSizeStyle.SMALL_38
    OneTapButtonSizeStyle.MEDIUM_40 -> OAuthListWidgetSizeStyle.MEDIUM_40
    OneTapButtonSizeStyle.MEDIUM_42 -> OAuthListWidgetSizeStyle.MEDIUM_42
    OneTapButtonSizeStyle.MEDIUM_44 -> OAuthListWidgetSizeStyle.MEDIUM_44
    OneTapButtonSizeStyle.MEDIUM_46 -> OAuthListWidgetSizeStyle.MEDIUM_46
    OneTapButtonSizeStyle.LARGE_48 -> OAuthListWidgetSizeStyle.LARGE_48
    OneTapButtonSizeStyle.LARGE_50 -> OAuthListWidgetSizeStyle.LARGE_50
    OneTapButtonSizeStyle.LARGE_52 -> OAuthListWidgetSizeStyle.LARGE_52
    OneTapButtonSizeStyle.LARGE_54 -> OAuthListWidgetSizeStyle.LARGE_54
    OneTapButtonSizeStyle.LARGE_56 -> OAuthListWidgetSizeStyle.LARGE_56
}
