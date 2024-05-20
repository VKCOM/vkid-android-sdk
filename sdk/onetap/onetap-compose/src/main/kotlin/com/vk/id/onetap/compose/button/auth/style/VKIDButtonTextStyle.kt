@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.button.auth.style

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.InternalVKIDButtonTextStyle
import com.vk.id.onetap.compose.R

@Composable
internal fun InternalVKIDButtonTextStyle.asColorResource() = when (this) {
    InternalVKIDButtonTextStyle.DARK -> colorResource(id = R.color.vkid_black)
    InternalVKIDButtonTextStyle.LIGHT -> colorResource(id = R.color.vkid_white)
}
