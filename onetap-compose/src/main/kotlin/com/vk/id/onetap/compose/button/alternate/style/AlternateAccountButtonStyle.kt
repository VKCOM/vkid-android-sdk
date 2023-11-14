package com.vk.id.onetap.compose.button.alternate.style

import com.vk.id.onetap.compose.button.VKIDButtonCornersStyle
import com.vk.id.onetap.compose.button.VKIDButtonElevationStyle
import com.vk.id.onetap.compose.button.VKIDButtonRippleStyle
import com.vk.id.onetap.compose.button.VKIDButtonSizeStyle

public sealed class AlternateAccountButtonStyle(
    public val backgroundStyle: AlternateAccountButtonBackgroundStyle,
    public val rippleStyle: VKIDButtonRippleStyle,
    public val textStyle: AlternateAccountButtonTextStyle,
    public val cornersStyle: VKIDButtonCornersStyle,
    public val sizeStyle: VKIDButtonSizeStyle,
    public val elevationStyle: VKIDButtonElevationStyle,
) {
    public class Light(
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : AlternateAccountButtonStyle(
        backgroundStyle = AlternateAccountButtonBackgroundStyle.LIGHT,
        rippleStyle = VKIDButtonRippleStyle.LIGHT,
        textStyle = AlternateAccountButtonTextStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class Dark(
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : AlternateAccountButtonStyle(
        backgroundStyle = AlternateAccountButtonBackgroundStyle.DARK,
        rippleStyle = VKIDButtonRippleStyle.DARK,
        textStyle = AlternateAccountButtonTextStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )
}
