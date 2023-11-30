package com.vk.id.sample.xml.onetap.data

import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.sample.xml.onetap.item.OneTapItem
import com.vk.id.sample.xml.uikit.item.HalfSpacerItem
import com.vk.id.sample.xml.uikit.item.SpacerItem
import com.vk.id.sample.xml.uikit.item.TitleItem

private const val ELEVATION_4_DP = 4f
private const val ELEVATION_8_DP = 8f

public val buttonStylingData: List<Any> = listOf(
    TitleItem("With multibranding"),
    HalfSpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.None,
        ),
        oAuths = setOf(OneTapOAuth.MAIL, OneTapOAuth.OK),
    ),
    HalfSpacerItem(),
    TitleItem("Primary"),
    HalfSpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.None,
        )
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Secondary light"),
    HalfSpacerItem(),
    OneTapItem(
        style = OneTapStyle.TransparentLight(
            cornersStyle = OneTapButtonCornersStyle.None,
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.TransparentLight(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.TransparentLight(
            cornersStyle = OneTapButtonCornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Secondary dark"),
    HalfSpacerItem(isDarkBackground = true),
    OneTapItem(
        style = OneTapStyle.TransparentDark(
            cornersStyle = OneTapButtonCornersStyle.None,
        ),
        isDarkBackground = true
    ),
    SpacerItem(isDarkBackground = true),
    OneTapItem(
        style = OneTapStyle.TransparentDark(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
        ),
        isDarkBackground = true
    ),
    SpacerItem(isDarkBackground = true),
    OneTapItem(
        style = OneTapStyle.TransparentDark(
            cornersStyle = OneTapButtonCornersStyle.Round,
        ),
        isDarkBackground = true
    ),
    HalfSpacerItem(isDarkBackground = true),
    TitleItem("Elevation"),
    HalfSpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            elevationStyle = OneTapButtonElevationStyle.Default
        )
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            elevationStyle = OneTapButtonElevationStyle.Custom(ELEVATION_4_DP)
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            elevationStyle = OneTapButtonElevationStyle.Custom(ELEVATION_8_DP)
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Sizes"),
    HalfSpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.SMALL_32
        )
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.SMALL_34
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.SMALL_36
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.SMALL_38
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_40
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_42
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_44
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_46
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.LARGE_48
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.LARGE_50
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.LARGE_52
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.LARGE_54
        ),
    ),
    SpacerItem(),
    OneTapItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.LARGE_56
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Small button"),
    HalfSpacerItem(),
    OneTapItem(
        style = OneTapStyle.Icon(
            cornersStyle = OneTapButtonCornersStyle.Default,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
            elevationStyle = OneTapButtonElevationStyle.Default
        ),
    ),
    HalfSpacerItem(),
)
