package com.vk.id.onetap.compose.button.alternate.style

import com.vk.id.onetap.compose.button.auth.style.VKIDButtonBorderStyle
import com.vk.id.onetap.compose.button.auth.style.VKIDButtonRippleStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonCornersStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonSizeStyle

internal sealed class AlternateAccountButtonStyle(
    val backgroundStyle: AlternateAccountButtonBackgroundStyle,
    val borderStyle: VKIDButtonBorderStyle,
    val rippleStyle: VKIDButtonRippleStyle,
    val textStyle: AlternateAccountButtonTextStyle,
    val cornersStyle: OneTapButtonCornersStyle,
    val sizeStyle: OneTapButtonSizeStyle,
) {
    class Light(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
    ) : AlternateAccountButtonStyle(
        backgroundStyle = AlternateAccountButtonBackgroundStyle.LIGHT,
        borderStyle = VKIDButtonBorderStyle.NONE,
        rippleStyle = VKIDButtonRippleStyle.LIGHT,
        textStyle = AlternateAccountButtonTextStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )

    class Dark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
    ) : AlternateAccountButtonStyle(
        backgroundStyle = AlternateAccountButtonBackgroundStyle.DARK,
        borderStyle = VKIDButtonBorderStyle.NONE,
        rippleStyle = VKIDButtonRippleStyle.DARK,
        textStyle = AlternateAccountButtonTextStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )
}
