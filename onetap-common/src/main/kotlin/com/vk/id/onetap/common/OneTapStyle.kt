@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.common

import com.vk.id.commn.InternalVKIDApi
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle
import com.vk.id.onetap.common.alternate.style.AlternateAccountButtonStyle
import com.vk.id.onetap.common.auth.style.VKIDButtonStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.common.mutlibranding.style.toOAuthListWidgetStyle

public sealed class OneTapStyle(
    public val cornersStyle: OneTapButtonCornersStyle,
    public val sizeStyle: OneTapButtonSizeStyle,
    public val elevationStyle: OneTapButtonElevationStyle,
    public val vkidButtonStyle: VKIDButtonStyle,
    public val alternateAccountButtonStyle: AlternateAccountButtonStyle,
    public val oAuthListWidgetStyle: OAuthListWidgetStyle,
) {
    public class Light(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.Light(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Light(cornersStyle, sizeStyle),
        oAuthListWidgetStyle = OAuthListWidgetStyle.Light(
            cornersStyle = cornersStyle.toOAuthListWidgetStyle(),
            sizeStyle = sizeStyle.toOAuthListWidgetStyle(),
        ),
    )

    public class Dark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.Dark(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Dark(cornersStyle, sizeStyle),
        oAuthListWidgetStyle = OAuthListWidgetStyle.Dark(
            cornersStyle = cornersStyle.toOAuthListWidgetStyle(),
            sizeStyle = sizeStyle.toOAuthListWidgetStyle(),
        ),
    )

    public class TransparentLight(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.TransparentLight(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.TransparentLight(cornersStyle, sizeStyle),
        oAuthListWidgetStyle = OAuthListWidgetStyle.Light(
            cornersStyle = cornersStyle.toOAuthListWidgetStyle(),
            sizeStyle = sizeStyle.toOAuthListWidgetStyle(),
        ),
    )

    public class TransparentDark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.TransparentDark(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.TransparentDark(cornersStyle, sizeStyle),
        oAuthListWidgetStyle = OAuthListWidgetStyle.Dark(
            cornersStyle = cornersStyle.toOAuthListWidgetStyle(),
            sizeStyle = sizeStyle.toOAuthListWidgetStyle(),
        ),
    )

    public class Icon(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.Light(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Light(cornersStyle, sizeStyle),
        oAuthListWidgetStyle = OAuthListWidgetStyle.Light(
            cornersStyle = cornersStyle.toOAuthListWidgetStyle(),
            sizeStyle = sizeStyle.toOAuthListWidgetStyle(),
        ),
    )
}
