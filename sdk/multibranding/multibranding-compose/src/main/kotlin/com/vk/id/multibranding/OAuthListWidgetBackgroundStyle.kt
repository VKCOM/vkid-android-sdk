package com.vk.id.multibranding

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle

@Suppress("ModifierComposed")
internal fun Modifier.background(
    style: OAuthListWidgetStyle,
): Modifier = composed {
    background(colorResource(if (style is OAuthListWidgetStyle.Light) R.color.vkid_white else R.color.vkid_transparent))
}
