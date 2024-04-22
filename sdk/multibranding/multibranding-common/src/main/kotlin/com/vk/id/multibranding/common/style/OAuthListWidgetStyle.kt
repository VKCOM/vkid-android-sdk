@file:OptIn(InternalVKIDApi::class)

package com.vk.id.multibranding.common.style

import android.content.Context
import androidx.compose.runtime.Immutable
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.util.vkidIsDarkTheme

/**
 * The style for OAuthListWidget.
 *
 * @param cornersStyle corner radius style.
 * @param rippleStyle ripple effect style.
 * @param borderStyle border style.
 * @param textStyle style of the text displayed of the widget.
 * @param sizeStyle denotes the size of the widget.
 */
@Immutable
public sealed class OAuthListWidgetStyle(
    public val cornersStyle: OAuthListWidgetCornersStyle,
    public val rippleStyle: VKIDOAuthListWidgetRippleStyle,
    public val borderStyle: VKIDOAuthListWidgetBorderStyle,
    public val textStyle: VKIDOAuthListWidgetTextStyle,
    public val sizeStyle: OAuthListWidgetSizeStyle,
) {
    /**
     * Light version, should be used for the dark layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     */
    public class Dark(
        cornersStyle: OAuthListWidgetCornersStyle = OAuthListWidgetCornersStyle.Default,
        sizeStyle: OAuthListWidgetSizeStyle = OAuthListWidgetSizeStyle.DEFAULT,
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = VKIDOAuthListWidgetRippleStyle.LIGHT,
        borderStyle = VKIDOAuthListWidgetBorderStyle.LIGHT,
        textStyle = VKIDOAuthListWidgetTextStyle.LIGHT,
        sizeStyle = sizeStyle,
    )

    /**
     * Dark version, should be used for the light layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     */
    public class Light(
        cornersStyle: OAuthListWidgetCornersStyle = OAuthListWidgetCornersStyle.Default,
        sizeStyle: OAuthListWidgetSizeStyle = OAuthListWidgetSizeStyle.DEFAULT,
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = VKIDOAuthListWidgetRippleStyle.DARK,
        borderStyle = VKIDOAuthListWidgetBorderStyle.DARK,
        textStyle = VKIDOAuthListWidgetTextStyle.DARK,
        sizeStyle = sizeStyle,
    )

    /** @suppress */
    public companion object {
        /**
         * Returns a style which is based on the current them.
         * The return value is either [Dark] or [Light]
         *
         * @param context Context which can be used to retrieve dark theme status.
         */
        public fun system(
            context: Context,
            cornersStyle: OAuthListWidgetCornersStyle = OAuthListWidgetCornersStyle.Default,
            sizeStyle: OAuthListWidgetSizeStyle = OAuthListWidgetSizeStyle.DEFAULT,
        ): OAuthListWidgetStyle {
            return (if (context.vkidIsDarkTheme) ::Dark else ::Light).invoke(cornersStyle, sizeStyle)
        }
    }
}
