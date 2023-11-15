package com.vk.id.multibranding

public sealed class OAuthListWidgetStyle(
    public val cornersStyle: OAuthListWidgetCornersStyle,
    public val rippleStyle: OAuthListWidgetRippleStyle,
    public val borderStyle: OAuthListWidgetBorderStyle,
    public val textStyle: OAuthListWidgetTextStyle,
) {
    public class Light(
        cornersStyle: OAuthListWidgetCornersStyle = OAuthListWidgetCornersStyle.Default
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = OAuthListWidgetRippleStyle.LIGHT,
        borderStyle = OAuthListWidgetBorderStyle.LIGHT,
        textStyle = OAuthListWidgetTextStyle.LIGHT,
    )

    public class Dark(
        cornersStyle: OAuthListWidgetCornersStyle = OAuthListWidgetCornersStyle.Default
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = OAuthListWidgetRippleStyle.DARK,
        borderStyle = OAuthListWidgetBorderStyle.DARK,
        textStyle = OAuthListWidgetTextStyle.DARK,
    )
}
