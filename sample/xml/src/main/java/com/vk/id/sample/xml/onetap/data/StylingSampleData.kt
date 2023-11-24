package com.vk.id.sample.xml.onetap.data

import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.sample.xml.onetap.item.ButtonItem
import com.vk.id.sample.xml.uikit.item.HalfSpacerItem
import com.vk.id.sample.xml.uikit.item.SpacerItem
import com.vk.id.sample.xml.uikit.item.TitleItem

public val buttonStylingData: List<Any> = listOf(
    TitleItem("Primary"),
    HalfSpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.None,
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Secondary light"),
    HalfSpacerItem(isDarkBackground = true),
    ButtonItem(
        style = OneTapStyle.TransparentLight(
            cornersStyle = OneTapButtonCornersStyle.None,
        ),
        isDarkBackground = true,
    ),
    SpacerItem(isDarkBackground = true),
    ButtonItem(
        style = OneTapStyle.TransparentLight(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
        ),
        isDarkBackground = true,
    ),
    SpacerItem(isDarkBackground = true),
    ButtonItem(
        style = OneTapStyle.TransparentLight(
            cornersStyle = OneTapButtonCornersStyle.Round,
        ),
        isDarkBackground = true,
    ),
    HalfSpacerItem(isDarkBackground = true),
    TitleItem("Secondary dark"),
    HalfSpacerItem(),
    ButtonItem(
        style = OneTapStyle.TransparentDark(
            cornersStyle = OneTapButtonCornersStyle.None,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.TransparentDark(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.TransparentDark(
            cornersStyle = OneTapButtonCornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Elevation"),
    HalfSpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            elevationStyle = OneTapButtonElevationStyle.Default
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            elevationStyle = OneTapButtonElevationStyle.Custom(4)
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            elevationStyle = OneTapButtonElevationStyle.Custom(8)
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Sizes"),
    HalfSpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.SMALL_32
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.SMALL_34
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.SMALL_36
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.SMALL_38
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_40
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_42
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_44
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_46
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.LARGE_48
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.LARGE_50
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.LARGE_52
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.LARGE_54
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.Light(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
            sizeStyle = OneTapButtonSizeStyle.LARGE_56
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Small button"),
    HalfSpacerItem(),
    ButtonItem(
        style = OneTapStyle.Icon(
            cornersStyle = OneTapButtonCornersStyle.Default,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
            elevationStyle = OneTapButtonElevationStyle.Default
        ),
    ),
    HalfSpacerItem(),
)
