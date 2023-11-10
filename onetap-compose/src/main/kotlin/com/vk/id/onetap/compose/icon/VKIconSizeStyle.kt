package com.vk.id.onetap.compose.icon

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.compose.button.VKIDButtonSizeStyle

public enum class VKIconSizeStyle {
    SMALL,
    NORMAL,
}

private const val VK_ICON_SMALL_SIZE_DP = 24
private const val VK_ICON_NORMAL_SIZE_DP = 28

public fun VKIconSizeStyle.asIconSize(): Int = when (this) {
    VKIconSizeStyle.SMALL -> VK_ICON_SMALL_SIZE_DP
    VKIconSizeStyle.NORMAL -> VK_ICON_NORMAL_SIZE_DP
}

internal fun VKIDButtonSizeStyle.asIconSizeStyle() = when (this) {
    VKIDButtonSizeStyle.DEFAULT -> VKIconSizeStyle.SMALL
    VKIDButtonSizeStyle.SMALL_32,
    VKIDButtonSizeStyle.SMALL_34,
    VKIDButtonSizeStyle.SMALL_36,
    VKIDButtonSizeStyle.SMALL_38 -> VKIconSizeStyle.NORMAL
    VKIDButtonSizeStyle.MEDIUM_40,
    VKIDButtonSizeStyle.MEDIUM_42,
    VKIDButtonSizeStyle.MEDIUM_44,
    VKIDButtonSizeStyle.MEDIUM_46,
    VKIDButtonSizeStyle.LARGE_48,
    VKIDButtonSizeStyle.LARGE_50,
    VKIDButtonSizeStyle.LARGE_52,
    VKIDButtonSizeStyle.LARGE_54,
    VKIDButtonSizeStyle.LARGE_56 -> VKIconSizeStyle.NORMAL
}

internal fun Modifier.size(style: VKIconSizeStyle) = size(style.asIconSize().dp)
