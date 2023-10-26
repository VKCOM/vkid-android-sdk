package com.vk.id.onetap.compose.icon

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

internal fun com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.asIconSizeStyle() = when (this) {
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.DEFAULT -> VKIconSizeStyle.NORMAL
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.SMALL_32,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.SMALL_34,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.SMALL_36,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.SMALL_38 -> VKIconSizeStyle.NORMAL
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.MEDIUM_40,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.MEDIUM_42,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.MEDIUM_44,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.MEDIUM_46,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.LARGE_48,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.LARGE_50,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.LARGE_52,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.LARGE_54,
    com.vk.id.onetap.compose.button.VKIDButtonSizeStyle.LARGE_56 -> VKIconSizeStyle.NORMAL
}

internal fun Modifier.size(style: VKIconSizeStyle) = size(style.asIconSize().dp)
