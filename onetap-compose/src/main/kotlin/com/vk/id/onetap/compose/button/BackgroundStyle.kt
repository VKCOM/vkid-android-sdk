package com.vk.id.onetap.compose.button

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import com.vk.id.onetap.compose.R

public enum class BackgroundStyle {
    BLUE,
    TRANSPARENT
}

internal fun Modifier.background(style: BackgroundStyle) = composed {
    val backgroundResource = when (style) {
        BackgroundStyle.BLUE -> R.color.vkid_azure_A100
        BackgroundStyle.TRANSPARENT -> R.color.vkid_transparent
    }
    background(colorResource(backgroundResource))
}
