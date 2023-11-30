package com.vk.id.onetap.compose.icon

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.vk.id.onetap.common.icon.style.VKIconColorStyle
import com.vk.id.onetap.compose.R

@Composable
@DrawableRes
internal fun VKIconColorStyle.asPainterResource() = when (this) {
    VKIconColorStyle.WHITE -> R.drawable.vkid_icon_white
    VKIconColorStyle.BLUE -> R.drawable.vkid_icon_blue
}
