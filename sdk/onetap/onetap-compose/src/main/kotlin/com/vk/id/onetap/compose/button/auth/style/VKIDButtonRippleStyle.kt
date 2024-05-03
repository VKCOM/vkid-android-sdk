@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.button.auth.style

import androidx.compose.ui.graphics.Color
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.InternalVKIDButtonRippleStyle

internal fun InternalVKIDButtonRippleStyle.asColor() = when (this) {
    InternalVKIDButtonRippleStyle.DARK -> Color.Black
    InternalVKIDButtonRippleStyle.LIGHT -> Color.White
}
