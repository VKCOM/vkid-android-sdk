package com.vk.id.onetap.compose.icon

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.common.icon.VKIconSizeStyle
import com.vk.id.onetap.common.icon.asIconSize

internal fun Modifier.size(style: VKIconSizeStyle) = size(style.asIconSize().dp)
