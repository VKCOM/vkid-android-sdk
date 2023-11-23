package com.vk.id.onetap.compose.progress

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import com.vk.id.onetap.compose.R

internal enum class CircleProgressStyle {
    LIGHT,
    DARK
}

@Composable
internal fun CircleProgressStyle.asColorFilter(): ColorFilter {
    val colorResource = when (this) {
        CircleProgressStyle.LIGHT -> R.color.vkid_white
        CircleProgressStyle.DARK -> R.color.vkid_black
    }
    return ColorFilter.tint(colorResource(id = colorResource))
}
