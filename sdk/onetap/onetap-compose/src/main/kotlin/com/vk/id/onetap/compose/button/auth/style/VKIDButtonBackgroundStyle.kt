@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.button.auth.style

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.VKIDButtonBackgroundStyle
import com.vk.id.onetap.compose.R

internal fun Modifier.background(style: VKIDButtonBackgroundStyle) = composed {
    val backgroundResource = when (style) {
        VKIDButtonBackgroundStyle.BLUE -> R.color.vkid_azure_A100
        VKIDButtonBackgroundStyle.TRANSPARENT -> R.color.vkid_transparent
    }
    background(colorResource(backgroundResource))
}
