package com.vk.id.multibranding

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.common.style.InternalVKIDOAuthListWidgetTextStyle

@Composable
@OptIn(InternalVKIDApi::class)
internal fun InternalVKIDOAuthListWidgetTextStyle.asColorResource() = when (this) {
    InternalVKIDOAuthListWidgetTextStyle.DARK -> Color.Black
    InternalVKIDOAuthListWidgetTextStyle.LIGHT -> colorResource(R.color.vkid_text_dark_primary)
}
