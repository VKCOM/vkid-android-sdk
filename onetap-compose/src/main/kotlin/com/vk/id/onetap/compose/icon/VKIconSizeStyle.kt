package com.vk.id.onetap.compose.icon

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.compose.onetap.style.OneTapButtonSizeStyle

internal enum class VKIconSizeStyle {
    SMALL,
    NORMAL,
}

private const val VK_ICON_SMALL_SIZE_DP = 24
private const val VK_ICON_NORMAL_SIZE_DP = 28

internal fun VKIconSizeStyle.asIconSize(): Int = when (this) {
    VKIconSizeStyle.SMALL -> VK_ICON_SMALL_SIZE_DP
    VKIconSizeStyle.NORMAL -> VK_ICON_NORMAL_SIZE_DP
}

internal fun OneTapButtonSizeStyle.asIconSizeStyle() = when (this) {
    OneTapButtonSizeStyle.DEFAULT -> VKIconSizeStyle.SMALL
    OneTapButtonSizeStyle.SMALL_32,
    OneTapButtonSizeStyle.SMALL_34,
    OneTapButtonSizeStyle.SMALL_36,
    OneTapButtonSizeStyle.SMALL_38 -> VKIconSizeStyle.NORMAL
    OneTapButtonSizeStyle.MEDIUM_40,
    OneTapButtonSizeStyle.MEDIUM_42,
    OneTapButtonSizeStyle.MEDIUM_44,
    OneTapButtonSizeStyle.MEDIUM_46,
    OneTapButtonSizeStyle.LARGE_48,
    OneTapButtonSizeStyle.LARGE_50,
    OneTapButtonSizeStyle.LARGE_52,
    OneTapButtonSizeStyle.LARGE_54,
    OneTapButtonSizeStyle.LARGE_56 -> VKIconSizeStyle.NORMAL
}

internal fun Modifier.size(style: VKIconSizeStyle) = size(style.asIconSize().dp)
