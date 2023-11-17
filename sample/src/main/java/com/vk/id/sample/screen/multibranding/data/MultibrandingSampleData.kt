package com.vk.id.sample.screen.multibranding.data

import com.vk.id.multibranding.OAuth
import com.vk.id.multibranding.OAuthListWidgetStyle
import com.vk.id.multibranding.WidgetCornersStyle
import com.vk.id.multibranding.WidgetSizeStyle
import com.vk.id.sample.screen.multibranding.item.OAuthListWidgetItem
import com.vk.id.sample.uikit.spacer.HalfSpacerItem
import com.vk.id.sample.uikit.spacer.SpacerItem
import com.vk.id.sample.uikit.spacer.TitleItem

internal val multibrandingSampleData = listOf(
    TitleItem("Multibranding widget"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
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
    TitleItem("Light widget"),
    HalfSpacerItem(isDarkBackground = true),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Light(),
        isDarkBackground = true
    ),
    HalfSpacerItem(isDarkBackground = true),
    TitleItem("Corner radius"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            cornersStyle = WidgetCornersStyle.None
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            cornersStyle = WidgetCornersStyle.Rounded
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            cornersStyle = WidgetCornersStyle.Round
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Sizes"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.SMALL_32
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.SMALL_34
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.SMALL_36
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.SMALL_38
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.MEDIUM_40
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.MEDIUM_42
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.MEDIUM_44
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.MEDIUM_46
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.LARGE_48
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.LARGE_50
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.LARGE_52
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.LARGE_54
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = WidgetSizeStyle.LARGE_56
        ),
    ),
    HalfSpacerItem(),
)
