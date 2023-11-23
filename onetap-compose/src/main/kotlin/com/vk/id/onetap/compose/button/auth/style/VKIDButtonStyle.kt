package com.vk.id.onetap.compose.button.auth.style

import com.vk.id.onetap.compose.icon.VKIconColorStyle
import com.vk.id.onetap.compose.icon.VKIconStyle
import com.vk.id.onetap.compose.icon.asIconSizeStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonCornersStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonElevationStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonSizeStyle
import com.vk.id.onetap.compose.progress.CircleProgressStyle

@Suppress("LongParameterList")
internal sealed class VKIDButtonStyle(
    val backgroundStyle: VKIDButtonBackgroundStyle,
    val rippleStyle: VKIDButtonRippleStyle,
    val borderStyle: VKIDButtonBorderStyle,
    val iconStyle: VKIconStyle,
    val textStyle: VKIDButtonTextStyle,
    val progressStyle: CircleProgressStyle,
    val cornersStyle: OneTapButtonCornersStyle,
    val sizeStyle: OneTapButtonSizeStyle,
    val elevationStyle: OneTapButtonElevationStyle,
) {
    class Blue(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
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

    class TransparentDark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
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

    class TransparentLight(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
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
