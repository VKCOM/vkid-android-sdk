package com.vk.id.multibranding

public sealed class OAuthListWidgetStyle(
    public val cornersStyle: OAuthListWidgetCornersStyle,
    public val rippleStyle: OAuthListWidgetRippleStyle,
    public val borderStyle: OAuthListWidgetBorderStyle,
    public val textStyle: OAuthListWidgetTextStyle,
    public val sizeStyle: OAuthListWidgetSizeStyle,
) {
    public class Light(
        cornersStyle: OAuthListWidgetCornersStyle = OAuthListWidgetCornersStyle.Default,
        sizeStyle: OAuthListWidgetSizeStyle = OAuthListWidgetSizeStyle.DEFAULT,
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = OAuthListWidgetRippleStyle.LIGHT,
        borderStyle = OAuthListWidgetBorderStyle.LIGHT,
        textStyle = OAuthListWidgetTextStyle.LIGHT,
        sizeStyle = sizeStyle,
    )

    public class Dark(
        cornersStyle: OAuthListWidgetCornersStyle = OAuthListWidgetCornersStyle.Default,
        sizeStyle: OAuthListWidgetSizeStyle = OAuthListWidgetSizeStyle.DEFAULT,
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = OAuthListWidgetRippleStyle.DARK,
        borderStyle = OAuthListWidgetBorderStyle.DARK,
        textStyle = OAuthListWidgetTextStyle.DARK,
        sizeStyle = sizeStyle,
    )
}
