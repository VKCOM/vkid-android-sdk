package com.vk.id.onetap.compose.button.auth.style

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.vk.id.onetap.common.auth.style.VKIDButtonTextStyle
import com.vk.id.onetap.compose.R

@Composable
internal fun VKIDButtonTextStyle.asColorResource() = when (this) {
    VKIDButtonTextStyle.DARK -> colorResource(id = R.color.vkid_black)
    VKIDButtonTextStyle.LIGHT -> colorResource(id = R.color.vkid_white)
}
