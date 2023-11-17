package com.vk.id.onetap.compose.icon

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.multibranding.WidgetSizeStyle

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

internal fun WidgetSizeStyle.asIconSizeStyle() = when (this) {
    WidgetSizeStyle.DEFAULT -> VKIconSizeStyle.SMALL
    WidgetSizeStyle.SMALL_32,
    WidgetSizeStyle.SMALL_34,
    WidgetSizeStyle.SMALL_36,
    WidgetSizeStyle.SMALL_38 -> VKIconSizeStyle.NORMAL
    WidgetSizeStyle.MEDIUM_40,
    WidgetSizeStyle.MEDIUM_42,
    WidgetSizeStyle.MEDIUM_44,
    WidgetSizeStyle.MEDIUM_46,
    WidgetSizeStyle.LARGE_48,
    WidgetSizeStyle.LARGE_50,
    WidgetSizeStyle.LARGE_52,
    WidgetSizeStyle.LARGE_54,
    WidgetSizeStyle.LARGE_56 -> VKIconSizeStyle.NORMAL
}

internal fun Modifier.size(style: VKIconSizeStyle) = size(style.asIconSize().dp)
