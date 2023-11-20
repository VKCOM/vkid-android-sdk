package com.vk.id.onetap.compose.button.alternate.style

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.vk.id.onetap.compose.R

public enum class AlternateAccountButtonTextStyle {
    LIGHT,
    DARK,
}

@Composable
internal fun AlternateAccountButtonTextStyle.asColorResource() = when (this) {
    AlternateAccountButtonTextStyle.LIGHT -> colorResource(id = R.color.vkid_azure_300)
    AlternateAccountButtonTextStyle.DARK -> colorResource(id = R.color.vkid_white)
}