@file:OptIn(InternalVKIDApi::class)

package com.vk.id.multibranding.common.style

import android.content.Context
import androidx.compose.runtime.Immutable
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.util.internalVkIdIsDarkTheme

/**
 * The style for OAuthListWidget.
 *
 * @param cornersStyle corner radius style.
 * @param rippleStyle ripple effect style.
 * @param borderStyle border style.
 * @param textStyle style of the text displayed of the widget.
 * @param sizeStyle denotes the size of the widget.
 *
 * @since 1.0.0
 */
@Immutable
public sealed class OAuthListWidgetStyle(
    @InternalVKIDApi public val cornersStyle: OAuthListWidgetCornersStyle,
    @InternalVKIDApi public val rippleStyle: InternalVKIDOAuthListWidgetRippleStyle,
    @InternalVKIDApi public val borderStyle: InternalVKIDOAuthListWidgetBorderStyle,
    @InternalVKIDApi public val textStyle: InternalVKIDOAuthListWidgetTextStyle,
    @InternalVKIDApi public val sizeStyle: OAuthListWidgetSizeStyle,
) {
    /**
     * Light version, should be used for the dark layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     *
     * @since 1.0.0
     */
    public class Dark(
        cornersStyle: OAuthListWidgetCornersStyle = OAuthListWidgetCornersStyle.Default,
        sizeStyle: OAuthListWidgetSizeStyle = OAuthListWidgetSizeStyle.DEFAULT,
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = InternalVKIDOAuthListWidgetRippleStyle.LIGHT,
        borderStyle = InternalVKIDOAuthListWidgetBorderStyle.LIGHT,
        textStyle = InternalVKIDOAuthListWidgetTextStyle.LIGHT,
        sizeStyle = sizeStyle,
    )

    /**
     * Dark version, should be used for the light layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     *
     *
     * @since 1.0.0
     * @since 1.0.0
     */
    public class Light(
        cornersStyle: OAuthListWidgetCornersStyle = OAuthListWidgetCornersStyle.Default,
        sizeStyle: OAuthListWidgetSizeStyle = OAuthListWidgetSizeStyle.DEFAULT,
    ) : OAuthListWidgetStyle(
        cornersStyle = cornersStyle,
        rippleStyle = InternalVKIDOAuthListWidgetRippleStyle.DARK,
        borderStyle = InternalVKIDOAuthListWidgetBorderStyle.DARK,
        textStyle = InternalVKIDOAuthListWidgetTextStyle.DARK,
        sizeStyle = sizeStyle,
    )

    /** @suppress */
    public companion object {
        /**
         * Returns a style which is based on the current them.
         * The return value is either [Dark] or [Light]
         *
         * @param context Context which can be used to retrieve dark theme status.
         *
         * @since 1.3.0
         */
        public fun system(
            context: Context,
            cornersStyle: OAuthListWidgetCornersStyle = OAuthListWidgetCornersStyle.Default,
            sizeStyle: OAuthListWidgetSizeStyle = OAuthListWidgetSizeStyle.DEFAULT,
        ): OAuthListWidgetStyle {
            return (if (context.internalVkIdIsDarkTheme) ::Dark else ::Light).invoke(cornersStyle, sizeStyle)
        }
    }
}
