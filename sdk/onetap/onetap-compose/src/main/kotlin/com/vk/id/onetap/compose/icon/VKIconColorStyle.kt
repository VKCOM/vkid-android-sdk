@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.icon

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.icon.style.InternalVKIconColorStyle
import com.vk.id.onetap.compose.R

@Composable
@DrawableRes
internal fun InternalVKIconColorStyle.asPainterResource() = when (this) {
    InternalVKIconColorStyle.WHITE -> R.drawable.vkid_icon_white
    InternalVKIconColorStyle.BLUE -> R.drawable.vkid_icon_blue
}
