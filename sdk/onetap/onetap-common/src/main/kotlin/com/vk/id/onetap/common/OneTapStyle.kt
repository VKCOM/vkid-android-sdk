@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.common

import android.content.Context
import androidx.compose.runtime.Immutable
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.util.internalVkIdIsDarkTheme
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle
import com.vk.id.onetap.common.alternate.style.InternalVKIDAlternateAccountButtonStyle
import com.vk.id.onetap.common.auth.style.InternalVKIDButtonStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.common.mutlibranding.style.toOAuthListWidgetStyle

/**
 * The style for OneTap widget.
 *
 * @param cornersStyle corner radius style.
 * @param sizeStyle denotes the size of the widget.
 * @param elevationStyle elevation of the button.
 * @param vkidButtonStyle style for the button.
 * @param alternateAccountButtonStyle style for the alternate account button.
 */
@Immutable
public sealed class OneTapStyle(
    @InternalVKIDApi public val cornersStyle: OneTapButtonCornersStyle,
    @InternalVKIDApi public val sizeStyle: OneTapButtonSizeStyle,
    @InternalVKIDApi public val elevationStyle: OneTapButtonElevationStyle,
    @InternalVKIDApi public val vkidButtonStyle: InternalVKIDButtonStyle,
    @InternalVKIDApi public val alternateAccountButtonStyle: InternalVKIDAlternateAccountButtonStyle,
) {
    @Suppress("DEPRECATION")
    @InternalVKIDApi
    public val oAuthListWidgetStyle: OAuthListWidgetStyle
        get() = when (this) {
            is Icon,
            is TransparentLight,
            is SecondaryLight,
            is Light -> OAuthListWidgetStyle.Light(
                cornersStyle = cornersStyle.toOAuthListWidgetStyle(),
                sizeStyle = sizeStyle.toOAuthListWidgetStyle(),
            )

            is TransparentDark,
            is SecondaryDark,
            is Dark -> OAuthListWidgetStyle.Dark(
                cornersStyle = cornersStyle.toOAuthListWidgetStyle(),
                sizeStyle = sizeStyle.toOAuthListWidgetStyle(),
            )
        }

    /**
     * Light version, should be used for the dark layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     * @param elevationStyle elevation of the button.
     */
    public class Light(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = InternalVKIDButtonStyle.Light(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = InternalVKIDAlternateAccountButtonStyle.Light(cornersStyle, sizeStyle),
    )

    /**
     * Dark version, should be used for the light layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     * @param elevationStyle elevation of the button.
     */
    public class Dark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = InternalVKIDButtonStyle.Dark(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = InternalVKIDAlternateAccountButtonStyle.Dark(cornersStyle, sizeStyle),
    )

    /**
     * Light version with white button, should be used for the dark layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     * @param elevationStyle elevation of the button.
     */
    public class SecondaryLight(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = InternalVKIDButtonStyle.Secondary(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = InternalVKIDAlternateAccountButtonStyle.Light(cornersStyle, sizeStyle),
    )

    /**
     * Dark version with white button, should be used for the light layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     * @param elevationStyle elevation of the button.
     */
    public class SecondaryDark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = InternalVKIDButtonStyle.Secondary(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = InternalVKIDAlternateAccountButtonStyle.Dark(cornersStyle, sizeStyle),
    )

    /**
     * Light version with transparent button, should be used for the dark layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     * @param elevationStyle elevation of the button.
     */
    @Deprecated(
        "This style will be removed in future releases. Please, use SecondaryLight instead.",
        ReplaceWith("SecondaryLight", "com.vk.id.onetap.common.SecondaryLight"),
    )
    public class TransparentLight(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = InternalVKIDButtonStyle.TransparentLight(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = InternalVKIDAlternateAccountButtonStyle.TransparentLight(cornersStyle, sizeStyle),
    )

    /**
     * Dark version with transparent button, should be used for the light layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     * @param elevationStyle elevation of the button.
     */
    @Deprecated(
        "This style will be removed in future releases. Please, use SecondaryDark instead.",
        ReplaceWith("SecondaryDark", "com.vk.id.onetap.common.SecondaryDark"),
    )
    public class TransparentDark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = InternalVKIDButtonStyle.TransparentDark(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = InternalVKIDAlternateAccountButtonStyle.TransparentDark(cornersStyle, sizeStyle),
    )

    /**
     * Icon version, can be used for any layout.
     *
     * @param cornersStyle corner radius style.
     * @param sizeStyle denotes the size of the widget.
     * @param elevationStyle elevation of the button.
     */
    public class Icon(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = InternalVKIDButtonStyle.Light(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = InternalVKIDAlternateAccountButtonStyle.Light(cornersStyle, sizeStyle),
    )

    /** @suppress */
    public companion object {
        /**
         * Returns a style which is based on the current theme.
         * The return value is either [Dark] or [Light]
         *
         * @param context Context which can be used to retrieve dark theme status.
         * @param cornersStyle corner radius style.
         * @param sizeStyle denotes the size of the widget.
         * @param elevationStyle elevation of the button.
         */
        public fun system(
            context: Context,
            cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
            sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
            elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
        ): OneTapStyle = (if (context.internalVkIdIsDarkTheme) ::Dark else ::Light)(
            cornersStyle,
            sizeStyle,
            elevationStyle,
        )

        /**
         * Returns a style which is based on the current theme.
         * The return value is either [SecondaryDark] or [SecondaryLight]
         *
         * @param context Context which can be used to retrieve dark theme status.
         * @param cornersStyle corner radius style.
         * @param sizeStyle denotes the size of the widget.
         * @param elevationStyle elevation of the button.
         */
        public fun secondarySystem(
            context: Context,
            cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
            sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
            elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
        ): OneTapStyle = (if (context.internalVkIdIsDarkTheme) ::SecondaryDark else ::SecondaryLight)(
            cornersStyle,
            sizeStyle,
            elevationStyle,
        )

        /**
         * Returns a style which is based on the current theme.
         * The return value is either [TransparentDark] or [TransparentLight]
         *
         * @param context Context which can be used to retrieve dark theme status.
         * @param cornersStyle corner radius style.
         * @param sizeStyle denotes the size of the widget.
         * @param elevationStyle elevation of the button.
         */
        @Deprecated(
            "This style will be removed in future releases. Please, use secondarySystem instead.",
            ReplaceWith("OneTapStyle.secondarySystem(context, cornersStyle, sizeStyle, elevationStyle)"),
        )
        @Suppress("DEPRECATION")
        public fun transparentSystem(
            context: Context,
            cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
            sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
            elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
        ): OneTapStyle = (if (context.internalVkIdIsDarkTheme) ::TransparentDark else ::TransparentLight)(
            cornersStyle,
            sizeStyle,
            elevationStyle,
        )
    }
}
