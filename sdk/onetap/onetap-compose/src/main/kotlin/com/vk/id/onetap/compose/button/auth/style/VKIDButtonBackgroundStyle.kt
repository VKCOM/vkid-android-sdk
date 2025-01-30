@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.button.auth.style

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.InternalVKIDButtonBackgroundStyle
import com.vk.id.onetap.compose.R

@Suppress("ModifierComposed")
internal fun Modifier.background(style: InternalVKIDButtonBackgroundStyle) = composed {
    val backgroundResource = when (style) {
        InternalVKIDButtonBackgroundStyle.BLUE -> R.color.vkid_azure_A100
        InternalVKIDButtonBackgroundStyle.WHITE -> R.color.vkid_white
        InternalVKIDButtonBackgroundStyle.TRANSPARENT -> R.color.vkid_transparent
    }
    background(colorResource(backgroundResource))
}
