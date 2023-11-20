package com.vk.id.onetap.compose.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.vk.id.onetap.compose.R

public enum class VKIDButtonTextStyle {
    LIGHT,
    DARK
}

@Composable
internal fun VKIDButtonTextStyle.asColorResource() = when (this) {
    VKIDButtonTextStyle.DARK -> colorResource(id = R.color.vkid_black)
    VKIDButtonTextStyle.LIGHT -> colorResource(id = R.color.vkid_white)
}
