package com.vk.id.sample.uikit.common

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import com.vk.id.sample.xml.R

internal fun Modifier.darkBackground(isDarkBackground: Boolean) = composed {
    if (isDarkBackground) {
        background(color = colorResource(id = R.color.vkid_gray900))
    } else {
        this
    }
}
