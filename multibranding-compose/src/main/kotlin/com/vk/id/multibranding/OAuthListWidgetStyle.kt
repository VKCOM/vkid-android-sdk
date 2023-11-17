package com.vk.id.multibranding

public sealed class OAuthListWidgetStyle(
    public val cornersStyle: WidgetCornersStyle,
    public val rippleStyle: WidgetRippleStyle,
    public val borderStyle: WidgetBorderStyle,
    public val textStyle: WidgetTextStyle,
    public val sizeStyle: WidgetSizeStyle,
) {
    public class Light(
        cornersStyle: WidgetCornersStyle = WidgetCornersStyle.Default,
        sizeStyle: WidgetSizeStyle = WidgetSizeStyle.DEFAULT,
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = WidgetRippleStyle.LIGHT,
        borderStyle = WidgetBorderStyle.LIGHT,
        textStyle = WidgetTextStyle.LIGHT,
        sizeStyle = sizeStyle,
    )

    public class Dark(
        cornersStyle: WidgetCornersStyle = WidgetCornersStyle.Default,
        sizeStyle: WidgetSizeStyle = WidgetSizeStyle.DEFAULT,
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = WidgetRippleStyle.DARK,
        borderStyle = WidgetBorderStyle.DARK,
        textStyle = WidgetTextStyle.DARK,
        sizeStyle = sizeStyle,
    )
}
