@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.progress

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.progress.style.InternalCircleProgressStyle
import com.vk.id.onetap.compose.R

@Composable
internal fun InternalCircleProgressStyle.asColorFilter(): ColorFilter {
    val colorResource = when (this) {
        InternalCircleProgressStyle.LIGHT -> R.color.vkid_white
        InternalCircleProgressStyle.DARK -> R.color.vkid_black
    }
    return ColorFilter.tint(colorResource(id = colorResource))
}
