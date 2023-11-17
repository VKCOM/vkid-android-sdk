package com.vk.id.multibranding

public sealed class OAuthListWidgetStyle(
    public val cornersStyle: CornersStyle,
    public val rippleStyle: RippleStyle,
    public val borderStyle: BorderStyle,
    public val textStyle: TextStyle,
) {
    public class Light(
        cornersStyle: CornersStyle = CornersStyle.Default
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = RippleStyle.LIGHT,
        borderStyle = BorderStyle.LIGHT,
        textStyle = TextStyle.LIGHT,
    )

    public class Dark(
        cornersStyle: CornersStyle = CornersStyle.Default
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = RippleStyle.DARK,
        borderStyle = BorderStyle.DARK,
        textStyle = TextStyle.DARK,
    )
}
