package com.vk.id.multibranding.common.style

/**
 * The style for OAuthListWidget.
 *
 * @param cornersStyle corner radius style.
 * @param rippleStyle ripple effect style.
 * @param borderStyle border style.
 * @param textStyle style of the text displayed of the widget.
 * @param sizeStyle denotes the size of the widget.
 */
public sealed class OAuthListWidgetStyle(
    public val cornersStyle: OAuthListWidgetCornersStyle,
    public val rippleStyle: OAuthListWidgetRippleStyle,
    public val borderStyle: OAuthListWidgetBorderStyle,
    public val textStyle: OAuthListWidgetTextStyle,
    public val sizeStyle: OAuthListWidgetSizeStyle,
) {
    /**
     * Light version, should be used for the dark layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     */
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

    /**
     * Dark version, should be used for the light layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     */
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
