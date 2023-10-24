package com.vk.id.onetap.compose.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import com.vk.id.onetap.common.R
import com.vk.id.onetap.common.icon.VKIconColorStyle

@Composable
internal fun VKIconColorStyle.asColorFilter(): ColorFilter {
    val colorResource = when (this) {
        VKIconColorStyle.WHITE -> R.color.vkid_white
        VKIconColorStyle.BLUE -> R.color.vkid_azure_A100
    }
    return ColorFilter.tint(colorResource(id = colorResource))
}
