package com.vk.id.onetap.compose.icon

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.vk.id.onetap.compose.R

public enum class VKIconColorStyle {
    WHITE,
    BLUE,
}

@Composable
@DrawableRes
internal fun VKIconColorStyle.asPainterResource() = when (this) {
    VKIconColorStyle.WHITE -> R.drawable.vkid_icon_white
    VKIconColorStyle.BLUE -> R.drawable.vkid_icon_blue
}
