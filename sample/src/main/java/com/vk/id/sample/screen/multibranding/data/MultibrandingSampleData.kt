package com.vk.id.sample.screen.multibranding.data

import com.vk.id.multibranding.OAuth
import com.vk.id.multibranding.OAuthListWidgetStyle
import com.vk.id.sample.screen.multibranding.item.OAuthListWidgetItem
import com.vk.id.sample.uikit.spacer.HalfSpacerItem
import com.vk.id.sample.uikit.spacer.SpacerItem
import com.vk.id.sample.uikit.spacer.TitleItem

internal val multibrandingSampleData = listOf(
    TitleItem("Multibranding widget"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        filter = { true }
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        filter = { it in setOf(OAuth.MAIL, OAuth.OK) }
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        filter = { it == OAuth.VK }
    ),
    HalfSpacerItem(),
)
