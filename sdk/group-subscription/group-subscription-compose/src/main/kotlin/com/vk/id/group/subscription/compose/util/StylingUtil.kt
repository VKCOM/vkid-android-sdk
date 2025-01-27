package com.vk.id.group.subscription.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.R

@Composable
internal fun textPrimaryColor(style: GroupSubscriptionStyle): Color {
    return when (style) {
        is GroupSubscriptionStyle.Light -> Color.Black
        is GroupSubscriptionStyle.Dark -> colorResource(R.color.vkid_text_dark_primary)
    }
}

@Composable
internal fun textSecondaryColor(style: GroupSubscriptionStyle): Color {
    return colorResource(
        when (style) {
            is GroupSubscriptionStyle.Light -> R.color.vkid_text_light_subhead
            is GroupSubscriptionStyle.Dark -> R.color.vkid_text_dark_subhead
        }
    )
}

internal fun textPrimaryButtonColor(style: GroupSubscriptionStyle): Color {
    return when (style) {
        is GroupSubscriptionStyle.Light -> Color.White
        is GroupSubscriptionStyle.Dark -> Color.Black
    }
}

@Composable
internal fun textPrimaryButtonBackgroundColor(style: GroupSubscriptionStyle): Color {
    return when (style) {
        is GroupSubscriptionStyle.Light -> colorResource(R.color.vkid_azure_300)
        is GroupSubscriptionStyle.Dark -> colorResource(R.color.vkid_white)
    }
}

@Composable
internal fun textSecondaryButtonBackgroundColor(style: GroupSubscriptionStyle): Color {
    return when (style) {
        is GroupSubscriptionStyle.Light -> colorResource(R.color.vkid_background_secondary_button_light)
        is GroupSubscriptionStyle.Dark -> colorResource(R.color.vkid_background_secondary_button_dark)
    }
}

@Composable
internal fun backgroundColor(style: GroupSubscriptionStyle): Color {
    return when (style) {
        is GroupSubscriptionStyle.Light -> colorResource(R.color.vkid_white)
        is GroupSubscriptionStyle.Dark -> colorResource(R.color.vkid_background_dark)
    }
}
