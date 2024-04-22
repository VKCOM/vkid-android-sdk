package com.vk.id.onetap.compose.button.alternate.style

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.alternate.style.VKIDAlternateAccountButtonBackgroundStyle
import com.vk.id.onetap.compose.R

@OptIn(InternalVKIDApi::class)
internal fun Modifier.background(style: VKIDAlternateAccountButtonBackgroundStyle) = composed {
    val backgroundResource = when (style) {
        VKIDAlternateAccountButtonBackgroundStyle.LIGHT -> R.color.vkid_ui_background_secondary_alpha
        VKIDAlternateAccountButtonBackgroundStyle.DARK -> R.color.vkid_ui_dark_background_secondary_alpha
        VKIDAlternateAccountButtonBackgroundStyle.TRANSPARENT -> R.color.vkid_transparent
    }
    background(colorResource(backgroundResource))
}
