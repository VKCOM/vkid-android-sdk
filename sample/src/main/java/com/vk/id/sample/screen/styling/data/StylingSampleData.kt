package com.vk.id.sample.screen.styling.data

import com.vk.id.multibranding.CornersStyle
import com.vk.id.onetap.compose.button.VKIDButtonElevationStyle
import com.vk.id.onetap.compose.button.VKIDButtonSizeStyle
import com.vk.id.onetap.compose.button.VKIDButtonStyle
import com.vk.id.sample.screen.styling.item.ButtonItem
import com.vk.id.sample.screen.styling.item.SmallButtonItem
import com.vk.id.sample.uikit.spacer.HalfSpacerItem
import com.vk.id.sample.uikit.spacer.SpacerItem
import com.vk.id.sample.uikit.spacer.TitleItem

internal val buttonStylingData = listOf(
    TitleItem("Primary"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.None,
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Secondary light"),
    HalfSpacerItem(isDarkBackground = true),
    ButtonItem(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = CornersStyle.None,
        ),
        isDarkBackground = true,
    ),
    SpacerItem(isDarkBackground = true),
    ButtonItem(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = CornersStyle.Rounded,
        ),
        isDarkBackground = true,
    ),
    SpacerItem(isDarkBackground = true),
    ButtonItem(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = CornersStyle.Round,
        ),
        isDarkBackground = true,
    ),
    HalfSpacerItem(isDarkBackground = true),
    TitleItem("Secondary dark"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = CornersStyle.None,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = CornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = CornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Elevation"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Default
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Custom(4)
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Custom(8)
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Sizes"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_32
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_34
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_36
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_38
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_40
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_42
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_44
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_46
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_48
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_50
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_52
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_54
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_56
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Small button"),
    HalfSpacerItem(),
    SmallButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = CornersStyle.Default,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
            elevationStyle = VKIDButtonElevationStyle.Default
        ),
    ),
    HalfSpacerItem(),
)
