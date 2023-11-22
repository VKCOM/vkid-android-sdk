package com.vk.id.onetap.compose.button.alternate.style

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import com.vk.id.onetap.compose.R

internal enum class AlternateAccountButtonBackgroundStyle {
    LIGHT,
    DARK
}

internal fun Modifier.background(style: AlternateAccountButtonBackgroundStyle) = composed {
    val backgroundResource = when (style) {
        AlternateAccountButtonBackgroundStyle.LIGHT -> R.color.vkid_ui_background_secondary_alpha
        AlternateAccountButtonBackgroundStyle.DARK -> R.color.vkid_ui_dark_background_secondary_alpha
    }
    background(colorResource(backgroundResource))
}
