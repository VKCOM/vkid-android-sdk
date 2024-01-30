package com.vk.id.sample.xml.multibranding.data

import android.content.Context
import com.vk.id.OAuth
import com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetSizeStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle
import com.vk.id.sample.xml.multibranding.item.OAuthListWidgetItem
import com.vk.id.sample.xml.uikit.item.HalfSpacerItem
import com.vk.id.sample.xml.uikit.item.SpacerItem
import com.vk.id.sample.xml.uikit.item.TitleItem

@Suppress("LongMethod")
public fun multibrandingSampleData(context: Context): List<Any> = listOf(
    TitleItem("Multibranding widget"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(context),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(context),
        oAuths = setOf(OAuth.MAIL, OAuth.OK)
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(context),
        oAuths = setOf(OAuth.VK)
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(context),
        oAuths = setOf(OAuth.MAIL)
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(context),
        oAuths = setOf(OAuth.OK)
    ),
    HalfSpacerItem(),
    TitleItem("Corner radius"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            cornersStyle = OAuthListWidgetCornersStyle.None
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            cornersStyle = OAuthListWidgetCornersStyle.Rounded
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            cornersStyle = OAuthListWidgetCornersStyle.Round
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Sizes"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.SMALL_32
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.SMALL_34
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.SMALL_36
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.SMALL_38
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_40
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_42
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_44
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_46
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.LARGE_48
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.LARGE_50
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.LARGE_52
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.LARGE_54
        ),
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(
            context = context,
            sizeStyle = OAuthListWidgetSizeStyle.LARGE_56
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Small widget"),
    HalfSpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(context),
        oAuths = setOf(OAuth.VK),
        width = 200,
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(context),
        oAuths = setOf(OAuth.VK),
        width = 240,
    ),
    SpacerItem(),
    OAuthListWidgetItem(
        style = OAuthListWidgetStyle.system(context),
        oAuths = setOf(OAuth.VK),
        width = 280,
    ),
    HalfSpacerItem(),
)
