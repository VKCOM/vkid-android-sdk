package com.vk.id.onetap.common.alternate.style

import androidx.compose.runtime.Immutable
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.InternalVKIDButtonBorderStyle
import com.vk.id.onetap.common.auth.style.InternalVKIDButtonRippleStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle

@Immutable
@InternalVKIDApi
public sealed class InternalVKIDAlternateAccountButtonStyle(
    public val backgroundStyle: InternalVKIDAlternateAccountButtonBackgroundStyle,
    public val borderStyle: InternalVKIDButtonBorderStyle,
    public val rippleStyle: InternalVKIDButtonRippleStyle,
    public val textStyle: InternalVKIDAlternateAccountButtonTextStyle,
    public val cornersStyle: OneTapButtonCornersStyle,
    public val sizeStyle: OneTapButtonSizeStyle,
) {
    @InternalVKIDApi
    public class Light(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
    ) : InternalVKIDAlternateAccountButtonStyle(
        backgroundStyle = InternalVKIDAlternateAccountButtonBackgroundStyle.LIGHT,
        borderStyle = InternalVKIDButtonBorderStyle.NONE,
        rippleStyle = InternalVKIDButtonRippleStyle.LIGHT,
        textStyle = InternalVKIDAlternateAccountButtonTextStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )

    @InternalVKIDApi
    public class Dark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
    ) : InternalVKIDAlternateAccountButtonStyle(
        backgroundStyle = InternalVKIDAlternateAccountButtonBackgroundStyle.DARK,
        borderStyle = InternalVKIDButtonBorderStyle.NONE,
        rippleStyle = InternalVKIDButtonRippleStyle.DARK,
        textStyle = InternalVKIDAlternateAccountButtonTextStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )

    @InternalVKIDApi
    public class TransparentLight(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
    ) : InternalVKIDAlternateAccountButtonStyle(
        backgroundStyle = InternalVKIDAlternateAccountButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = InternalVKIDButtonRippleStyle.DARK,
        borderStyle = InternalVKIDButtonBorderStyle.DARK,
        textStyle = InternalVKIDAlternateAccountButtonTextStyle.TRANSPARENT_DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )

    @InternalVKIDApi
    public class TransparentDark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
    ) : InternalVKIDAlternateAccountButtonStyle(
        backgroundStyle = InternalVKIDAlternateAccountButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = InternalVKIDButtonRippleStyle.LIGHT,
        borderStyle = InternalVKIDButtonBorderStyle.LIGHT,
        textStyle = InternalVKIDAlternateAccountButtonTextStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
    )
}
