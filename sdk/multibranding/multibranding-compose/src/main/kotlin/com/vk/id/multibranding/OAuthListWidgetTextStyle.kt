package com.vk.id.multibranding

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.common.style.InternalVKIDOAuthListWidgetTextStyle

@Composable
@OptIn(InternalVKIDApi::class)
internal fun InternalVKIDOAuthListWidgetTextStyle.asColorResource() = when (this) {
    InternalVKIDOAuthListWidgetTextStyle.DARK -> Color.Black
    InternalVKIDOAuthListWidgetTextStyle.LIGHT -> Color.White
}
