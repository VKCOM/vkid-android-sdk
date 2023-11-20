package com.vk.id.sample.screen.styling.data

import com.vk.id.onetap.compose.button.VKIDButtonCornersStyle
import com.vk.id.onetap.compose.button.VKIDButtonElevationStyle
import com.vk.id.onetap.compose.button.VKIDButtonSizeStyle
import com.vk.id.onetap.compose.button.VKIDButtonStyle
import com.vk.id.sample.screen.styling.item.ButtonItem
import com.vk.id.sample.screen.styling.item.SmallButtonItem
import com.vk.id.sample.uikit.item.HalfSpacerItem
import com.vk.id.sample.uikit.item.SpacerItem
import com.vk.id.sample.uikit.item.TitleItem

internal val buttonStylingData = listOf(
    TitleItem("Primary"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.None,
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Secondary light"),
    HalfSpacerItem(isDarkBackground = true),
    ButtonItem(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = VKIDButtonCornersStyle.None,
        ),
        isDarkBackground = true,
    ),
    SpacerItem(isDarkBackground = true),
    ButtonItem(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
        ),
        isDarkBackground = true,
    ),
    SpacerItem(isDarkBackground = true),
    ButtonItem(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = VKIDButtonCornersStyle.Round,
        ),
        isDarkBackground = true,
    ),
    HalfSpacerItem(isDarkBackground = true),
    TitleItem("Secondary dark"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = VKIDButtonCornersStyle.None,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = VKIDButtonCornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Elevation"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Default
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Custom(4)
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Custom(8)
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Sizes"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_32
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_34
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_36
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_38
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_40
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_42
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_44
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_46
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_48
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_50
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_52
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_54
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_56
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Small button"),
    HalfSpacerItem(),
    SmallButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Default,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
            elevationStyle = VKIDButtonElevationStyle.Default
        ),
    ),
    HalfSpacerItem(),
)
