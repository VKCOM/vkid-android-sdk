package com.vk.id.onetap.compose.button.alternate.style

import com.vk.id.onetap.compose.button.VKIDButtonBorderStyle
import com.vk.id.onetap.compose.button.VKIDButtonCornersStyle
import com.vk.id.onetap.compose.button.VKIDButtonRippleStyle
import com.vk.id.onetap.compose.button.VKIDButtonSizeStyle

internal sealed class AlternateAccountButtonStyle(
    val backgroundStyle: AlternateAccountButtonBackgroundStyle,
    val borderStyle: VKIDButtonBorderStyle,
    val rippleStyle: VKIDButtonRippleStyle,
    val textStyle: AlternateAccountButtonTextStyle,
    val cornersStyle: VKIDButtonCornersStyle,
    val sizeStyle: VKIDButtonSizeStyle,
) {
    class Light(
        borderStyle: VKIDButtonBorderStyle = VKIDButtonBorderStyle.NONE,
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
    ) : AlternateAccountButtonStyle(
        backgroundStyle = AlternateAccountButtonBackgroundStyle.LIGHT,
        borderStyle = borderStyle,
        rippleStyle = VKIDButtonRippleStyle.LIGHT,
        textStyle = AlternateAccountButtonTextStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )

    class Dark(
        borderStyle: VKIDButtonBorderStyle = VKIDButtonBorderStyle.NONE,
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
    ) : AlternateAccountButtonStyle(
        backgroundStyle = AlternateAccountButtonBackgroundStyle.DARK,
        borderStyle = borderStyle,
        rippleStyle = VKIDButtonRippleStyle.DARK,
        textStyle = AlternateAccountButtonTextStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )
}
