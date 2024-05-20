package com.vk.id.onetap.compose.button.alternate.style

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.alternate.style.InternalVKIDAlternateAccountButtonTextStyle
import com.vk.id.onetap.compose.R

@OptIn(InternalVKIDApi::class)
@Composable
internal fun InternalVKIDAlternateAccountButtonTextStyle.asColorResource() = when (this) {
    InternalVKIDAlternateAccountButtonTextStyle.LIGHT -> colorResource(id = R.color.vkid_azure_300)
    InternalVKIDAlternateAccountButtonTextStyle.DARK -> colorResource(id = R.color.vkid_white)
    InternalVKIDAlternateAccountButtonTextStyle.TRANSPARENT_DARK -> colorResource(id = R.color.vkid_black)
}
