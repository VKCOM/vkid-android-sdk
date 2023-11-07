package com.vk.id.onetap.compose.button

import com.vk.id.onetap.compose.icon.VKIconColorStyle
import com.vk.id.onetap.compose.icon.VKIconStyle
import com.vk.id.onetap.compose.icon.asIconSizeStyle
import com.vk.id.onetap.compose.progress.CircleProgressStyle

@Suppress("LongParameterList")
public sealed class VKIDButtonStyle(
    public val backgroundStyle: VKIDButtonBackgroundStyle,
    public val rippleStyle: VKIDButtonRippleStyle,
    public val borderStyle: VKIDButtonBorderStyle,
    public val iconStyle: VKIconStyle,
    public val textStyle: VKIDButtonTextStyle,
    public val progressStyle: CircleProgressStyle,
    public val cornersStyle: VKIDButtonCornersStyle,
    public val sizeStyle: VKIDButtonSizeStyle,
    public val elevationStyle: VKIDButtonElevationStyle,
) {
    public class Blue(
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.BLUE,
        rippleStyle = VKIDButtonRippleStyle.LIGHT,
        borderStyle = VKIDButtonBorderStyle.NONE,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.WHITE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = VKIDButtonTextStyle.LIGHT,
        progressStyle = CircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class TransparentDark(
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = VKIDButtonRippleStyle.DARK,
        borderStyle = VKIDButtonBorderStyle.DARK,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.BLUE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = VKIDButtonTextStyle.DARK,
        progressStyle = CircleProgressStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class TransparentLight(
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = VKIDButtonRippleStyle.LIGHT,
        borderStyle = VKIDButtonBorderStyle.LIGHT,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.BLUE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = VKIDButtonTextStyle.LIGHT,
        progressStyle = CircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )
}
