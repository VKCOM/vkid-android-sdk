package com.vk.id.multibranding

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.common.style.VKIDOAuthListWidgetTextStyle

@Composable
@OptIn(InternalVKIDApi::class)
internal fun VKIDOAuthListWidgetTextStyle.asColorResource() = when (this) {
    VKIDOAuthListWidgetTextStyle.DARK -> Color.Black
    VKIDOAuthListWidgetTextStyle.LIGHT -> Color.White
}
