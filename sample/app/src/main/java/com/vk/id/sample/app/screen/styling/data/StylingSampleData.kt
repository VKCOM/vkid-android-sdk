package com.vk.id.sample.app.screen.styling.data

import com.vk.id.onetap.compose.onetap.OneTapStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonCornersStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonElevationStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonSizeStyle
import com.vk.id.sample.app.screen.styling.item.ButtonItem
import com.vk.id.sample.app.screen.styling.item.IconButtonItem
import com.vk.id.sample.xml.uikit.item.HalfSpacerItem
import com.vk.id.sample.xml.uikit.item.SpacerItem
import com.vk.id.sample.xml.uikit.item.TitleItem

internal val buttonStylingData = listOf(
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
    HalfSpacerItem(),
    ButtonItem(
        style = OneTapStyle.TransparentLight(
            cornersStyle = OneTapButtonCornersStyle.None,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.TransparentLight(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = OneTapStyle.TransparentLight(
            cornersStyle = OneTapButtonCornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Secondary dark"),
    HalfSpacerItem(isDarkBackground = true),
    ButtonItem(
        style = OneTapStyle.TransparentDark(
            cornersStyle = OneTapButtonCornersStyle.None,
        ),
        isDarkBackground = true
    ),
    SpacerItem(isDarkBackground = true),
    ButtonItem(
        style = OneTapStyle.TransparentDark(
            cornersStyle = OneTapButtonCornersStyle.Rounded,
        ),
        isDarkBackground = true
    ),
    SpacerItem(isDarkBackground = true),
    ButtonItem(
        style = OneTapStyle.TransparentDark(
            cornersStyle = OneTapButtonCornersStyle.Round,
        ),
        isDarkBackground = true
    ),
    HalfSpacerItem(isDarkBackground = true),
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
    IconButtonItem(
        style = OneTapStyle.Icon(
            cornersStyle = OneTapButtonCornersStyle.Default,
            sizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
            elevationStyle = OneTapButtonElevationStyle.Default
        ),
    ),
    HalfSpacerItem(),
)