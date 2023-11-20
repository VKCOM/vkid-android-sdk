package com.vk.id.onetap.compose.button.alternate.style

import com.vk.id.multibranding.WidgetBorderStyle
import com.vk.id.multibranding.WidgetCornersStyle
import com.vk.id.multibranding.WidgetRippleStyle
import com.vk.id.multibranding.WidgetSizeStyle

public sealed class AlternateAccountButtonStyle(
    public val backgroundStyle: AlternateAccountButtonBackgroundStyle,
    public val borderStyle: WidgetBorderStyle,
    public val rippleStyle: WidgetRippleStyle,
    public val textStyle: AlternateAccountButtonTextStyle,
    public val cornersStyle: WidgetCornersStyle,
    public val sizeStyle: WidgetSizeStyle,
) {
    public class Light(
        borderStyle: WidgetBorderStyle = WidgetBorderStyle.NONE,
        cornersStyle: WidgetCornersStyle = WidgetCornersStyle.Default,
        sizeStyle: WidgetSizeStyle = WidgetSizeStyle.MEDIUM_44,
    ) : AlternateAccountButtonStyle(
        backgroundStyle = AlternateAccountButtonBackgroundStyle.LIGHT,
        borderStyle = borderStyle,
        rippleStyle = WidgetRippleStyle.LIGHT,
        textStyle = AlternateAccountButtonTextStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )

    public class Dark(
        borderStyle: WidgetBorderStyle = WidgetBorderStyle.NONE,
        cornersStyle: WidgetCornersStyle = WidgetCornersStyle.Default,
        sizeStyle: WidgetSizeStyle = WidgetSizeStyle.MEDIUM_44,
    ) : AlternateAccountButtonStyle(
        backgroundStyle = AlternateAccountButtonBackgroundStyle.DARK,
        borderStyle = borderStyle,
        rippleStyle = WidgetRippleStyle.DARK,
        textStyle = AlternateAccountButtonTextStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )
}
