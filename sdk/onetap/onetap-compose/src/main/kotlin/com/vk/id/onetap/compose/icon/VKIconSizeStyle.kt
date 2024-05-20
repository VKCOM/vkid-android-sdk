@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.icon

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.icon.style.InternalVKIconSizeStyle

private const val VK_ICON_SMALL_SIZE_DP = 24
private const val VK_ICON_NORMAL_SIZE_DP = 28

internal fun InternalVKIconSizeStyle.asIconSize(): Int = when (this) {
    InternalVKIconSizeStyle.SMALL -> VK_ICON_SMALL_SIZE_DP
    InternalVKIconSizeStyle.NORMAL -> VK_ICON_NORMAL_SIZE_DP
}

internal fun Modifier.size(style: InternalVKIconSizeStyle) = size(style.asIconSize().dp)
