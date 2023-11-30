package com.vk.id.onetap.compose.button.alternate.style

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.onetap.common.alternate.style.AlternateAccountButtonTextStyle
import com.vk.id.onetap.compose.R

@OptIn(InternalVKIDApi::class)
@Composable
internal fun AlternateAccountButtonTextStyle.asColorResource() = when (this) {
    AlternateAccountButtonTextStyle.LIGHT -> colorResource(id = R.color.vkid_azure_300)
    AlternateAccountButtonTextStyle.DARK -> colorResource(id = R.color.vkid_white)
    AlternateAccountButtonTextStyle.TRANSPARENT_DARK -> colorResource(id = R.color.vkid_black)
}
