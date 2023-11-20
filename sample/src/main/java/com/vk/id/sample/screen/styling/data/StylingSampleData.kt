package com.vk.id.sample.screen.styling.data

import com.vk.id.multibranding.WidgetCornersStyle
import com.vk.id.multibranding.WidgetSizeStyle
import com.vk.id.onetap.compose.button.VKIDButtonStyle
import com.vk.id.onetap.compose.button.WigetElevationStyle
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
            cornersStyle = WidgetCornersStyle.None,
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Secondary light"),
    HalfSpacerItem(isDarkBackground = true),
    ButtonItem(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = WidgetCornersStyle.None,
        ),
        isDarkBackground = true,
    ),
    SpacerItem(isDarkBackground = true),
    ButtonItem(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = WidgetCornersStyle.Rounded,
        ),
        isDarkBackground = true,
    ),
    SpacerItem(isDarkBackground = true),
    ButtonItem(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = WidgetCornersStyle.Round,
        ),
        isDarkBackground = true,
    ),
    HalfSpacerItem(isDarkBackground = true),
    TitleItem("Secondary dark"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = WidgetCornersStyle.None,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = WidgetCornersStyle.Rounded,
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = WidgetCornersStyle.Round,
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Elevation"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            elevationStyle = WigetElevationStyle.Default
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            elevationStyle = WigetElevationStyle.Custom(4)
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            elevationStyle = WigetElevationStyle.Custom(8)
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Sizes"),
    HalfSpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.SMALL_32
        )
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.SMALL_34
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.SMALL_36
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.SMALL_38
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.MEDIUM_40
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.MEDIUM_42
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.MEDIUM_44
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.MEDIUM_46
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.LARGE_48
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.LARGE_50
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.LARGE_52
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.LARGE_54
        ),
    ),
    SpacerItem(),
    ButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Rounded,
            sizeStyle = WidgetSizeStyle.LARGE_56
        ),
    ),
    HalfSpacerItem(),
    TitleItem("Small button"),
    HalfSpacerItem(),
    SmallButtonItem(
        style = VKIDButtonStyle.Blue(
            cornersStyle = WidgetCornersStyle.Default,
            sizeStyle = WidgetSizeStyle.MEDIUM_44,
            elevationStyle = WigetElevationStyle.Default
        ),
    ),
    HalfSpacerItem(),
)
