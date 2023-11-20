package com.vk.id.onetap.compose.button.alternate.style

import com.vk.id.onetap.compose.button.VKIDButtonBorderStyle
import com.vk.id.onetap.compose.button.VKIDButtonCornersStyle
import com.vk.id.onetap.compose.button.VKIDButtonRippleStyle
import com.vk.id.onetap.compose.button.VKIDButtonSizeStyle

public sealed class AlternateAccountButtonStyle(
    public val backgroundStyle: AlternateAccountButtonBackgroundStyle,
    public val borderStyle: VKIDButtonBorderStyle,
    public val rippleStyle: VKIDButtonRippleStyle,
    public val textStyle: AlternateAccountButtonTextStyle,
    public val cornersStyle: VKIDButtonCornersStyle,
    public val sizeStyle: VKIDButtonSizeStyle,
) {
    public class Light(
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

    public class Dark(
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
