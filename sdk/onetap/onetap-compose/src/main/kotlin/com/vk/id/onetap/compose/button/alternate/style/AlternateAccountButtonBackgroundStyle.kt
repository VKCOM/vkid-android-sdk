package com.vk.id.onetap.compose.button.alternate.style

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.alternate.style.AlternateAccountButtonBackgroundStyle
import com.vk.id.onetap.compose.R

@OptIn(InternalVKIDApi::class)
internal fun Modifier.background(style: AlternateAccountButtonBackgroundStyle) = composed {
    val backgroundResource = when (style) {
        AlternateAccountButtonBackgroundStyle.LIGHT -> R.color.vkid_ui_background_secondary_alpha
        AlternateAccountButtonBackgroundStyle.DARK -> R.color.vkid_ui_dark_background_secondary_alpha
        AlternateAccountButtonBackgroundStyle.TRANSPARENT -> R.color.vkid_transparent
    }
    background(colorResource(backgroundResource))
}
