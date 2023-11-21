package com.vk.id.sample.screen.multibranding.data

import com.vk.id.multibranding.OAuth
import com.vk.id.multibranding.OAuthListWidgetCornersStyle
import com.vk.id.multibranding.OAuthListWidgetSizeStyle
import com.vk.id.multibranding.OAuthListWidgetStyle
import com.vk.id.sample.screen.multibranding.item.OAuthListWidgetItem
import com.vk.id.sample.uikit.item.HalfSpacerItem
import com.vk.id.sample.uikit.item.SpacerItem
import com.vk.id.sample.uikit.item.TitleItem

internal val multibrandingSampleData = listOf(
    TitleItem("Multibranding widget"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        allowedOAuths = setOf(OAuth.MAIL, OAuth.OK)
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        allowedOAuths = setOf(OAuth.VK)
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        allowedOAuths = setOf(OAuth.MAIL)
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        allowedOAuths = setOf(OAuth.OK)
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
            cornersStyle = OAuthListWidgetCornersStyle.None
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            cornersStyle = OAuthListWidgetCornersStyle.Rounded
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            cornersStyle = OAuthListWidgetCornersStyle.Round
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Sizes"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.SMALL_32
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.SMALL_34
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.SMALL_36
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.SMALL_38
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_40
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_42
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_44
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_46
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.LARGE_48
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.LARGE_50
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.LARGE_52
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.LARGE_54
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(
            sizeStyle = OAuthListWidgetSizeStyle.LARGE_56
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Small widget"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        allowedOAuths = setOf(OAuth.VK),
        width = 100,
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        allowedOAuths = setOf(OAuth.VK),
        width = 200,
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        allowedOAuths = setOf(OAuth.VK),
        width = 240,
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.Dark(),
        allowedOAuths = setOf(OAuth.VK),
        width = 280,
    ),
    HalfSpacerItem(),
)
