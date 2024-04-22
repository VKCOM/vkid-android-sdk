package com.vk.id.onetap.common.alternate.style

import androidx.compose.runtime.Immutable
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.VKIDButtonBorderStyle
import com.vk.id.onetap.common.auth.style.VKIDButtonRippleStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle

@Immutable
@InternalVKIDApi
public sealed class VKIDAlternateAccountButtonStyle(
    public val backgroundStyle: VKIDAlternateAccountButtonBackgroundStyle,
    public val borderStyle: VKIDButtonBorderStyle,
    public val rippleStyle: VKIDButtonRippleStyle,
    public val textStyle: VKIDAlternateAccountButtonTextStyle,
    public val cornersStyle: OneTapButtonCornersStyle,
    public val sizeStyle: OneTapButtonSizeStyle,
) {
    @InternalVKIDApi
    public class Light(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
    ) : VKIDAlternateAccountButtonStyle(
        backgroundStyle = VKIDAlternateAccountButtonBackgroundStyle.LIGHT,
        borderStyle = VKIDButtonBorderStyle.NONE,
        rippleStyle = VKIDButtonRippleStyle.LIGHT,
        textStyle = VKIDAlternateAccountButtonTextStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )

    @InternalVKIDApi
    public class Dark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
    ) : VKIDAlternateAccountButtonStyle(
        backgroundStyle = VKIDAlternateAccountButtonBackgroundStyle.DARK,
        borderStyle = VKIDButtonBorderStyle.NONE,
        rippleStyle = VKIDButtonRippleStyle.DARK,
        textStyle = VKIDAlternateAccountButtonTextStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )

    @InternalVKIDApi
    public class TransparentLight(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
    ) : VKIDAlternateAccountButtonStyle(
        backgroundStyle = VKIDAlternateAccountButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = VKIDButtonRippleStyle.DARK,
        borderStyle = VKIDButtonBorderStyle.DARK,
        textStyle = VKIDAlternateAccountButtonTextStyle.TRANSPARENT_DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )

    @InternalVKIDApi
    public class TransparentDark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
    ) : VKIDAlternateAccountButtonStyle(
        backgroundStyle = VKIDAlternateAccountButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = VKIDButtonRippleStyle.LIGHT,
        borderStyle = VKIDButtonBorderStyle.LIGHT,
        textStyle = VKIDAlternateAccountButtonTextStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )
}
