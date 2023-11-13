package com.vk.id.sample.styling

import com.vk.id.onetap.compose.button.VKIDButtonCornersStyle
import com.vk.id.onetap.compose.button.VKIDButtonElevationStyle
import com.vk.id.onetap.compose.button.VKIDButtonSizeStyle
import com.vk.id.onetap.compose.button.VKIDButtonStyle

internal val buttonStylingData = listOf(
    ListItem.Title("Primary"),
    ListItem.HalfSpacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.None,
        )
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Round,
        ),
    ),
    ListItem.HalfSpacer(),
    ListItem.Title("Secondary light"),
    ListItem.HalfSpacer(isDarkBackground = true),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = VKIDButtonCornersStyle.None,
        ),
        isDarkBackground = true,
    ),
    ListItem.Spacer(isDarkBackground = true),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
        ),
        isDarkBackground = true,
    ),
    ListItem.Spacer(isDarkBackground = true),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = VKIDButtonCornersStyle.Round,
        ),
        isDarkBackground = true,
    ),
    ListItem.HalfSpacer(isDarkBackground = true),
    ListItem.Title("Secondary dark"),
    ListItem.HalfSpacer(),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = VKIDButtonCornersStyle.None,
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = VKIDButtonCornersStyle.Round,
        ),
    ),
    ListItem.HalfSpacer(),
    ListItem.Title("Elevation"),
    ListItem.HalfSpacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Default
        )
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Custom(4)
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Custom(8)
        ),
    ),
    ListItem.HalfSpacer(),
    ListItem.Title("Sizes"),
    ListItem.HalfSpacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_32
        )
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_34
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_36
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_38
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_40
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_42
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_44
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_46
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_48
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_50
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_52
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_54
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_56
        ),
    ),
    ListItem.HalfSpacer(),
    ListItem.Title("Small button"),
    ListItem.HalfSpacer(),
    ListItem.SmallButton(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Default,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
            elevationStyle = VKIDButtonElevationStyle.Default
        ),
    ),
    ListItem.HalfSpacer(),
)
