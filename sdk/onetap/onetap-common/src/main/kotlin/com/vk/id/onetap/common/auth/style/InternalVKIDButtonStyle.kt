package com.vk.id.onetap.common.auth.style

import androidx.compose.runtime.Immutable
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.common.button.style.internalVKIDAsIconSizeStyle
import com.vk.id.onetap.common.icon.style.InternalVKIconColorStyle
import com.vk.id.onetap.common.icon.style.InternalVKIconStyle
import com.vk.id.onetap.common.progress.style.InternalCircleProgressStyle

@Suppress("LongParameterList")
@Immutable
@InternalVKIDApi
public sealed class InternalVKIDButtonStyle(
    public val backgroundStyle: InternalVKIDButtonBackgroundStyle,
    public val rippleStyle: InternalVKIDButtonRippleStyle,
    public val borderStyle: InternalVKIDButtonBorderStyle,
    public val iconStyle: InternalVKIconStyle,
    public val textStyle: InternalVKIDButtonTextStyle,
    public val progressStyle: InternalCircleProgressStyle,
    public val cornersStyle: OneTapButtonCornersStyle,
    public val sizeStyle: OneTapButtonSizeStyle,
    public val elevationStyle: OneTapButtonElevationStyle,
) {
    public class Light(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : InternalVKIDButtonStyle(
        backgroundStyle = InternalVKIDButtonBackgroundStyle.BLUE,
        rippleStyle = InternalVKIDButtonRippleStyle.LIGHT,
        borderStyle = InternalVKIDButtonBorderStyle.NONE,
        iconStyle = InternalVKIconStyle(
            colorStyle = InternalVKIconColorStyle.WHITE,
            sizeStyle = sizeStyle.internalVKIDAsIconSizeStyle(),
        ),
        textStyle = InternalVKIDButtonTextStyle.LIGHT,
        progressStyle = InternalCircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class Dark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : InternalVKIDButtonStyle(
        backgroundStyle = InternalVKIDButtonBackgroundStyle.BLUE,
        rippleStyle = InternalVKIDButtonRippleStyle.LIGHT,
        borderStyle = InternalVKIDButtonBorderStyle.NONE,
        iconStyle = InternalVKIconStyle(
            colorStyle = InternalVKIconColorStyle.WHITE,
            sizeStyle = sizeStyle.internalVKIDAsIconSizeStyle(),
        ),
        textStyle = InternalVKIDButtonTextStyle.LIGHT,
        progressStyle = InternalCircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class TransparentLight(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : InternalVKIDButtonStyle(
        backgroundStyle = InternalVKIDButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = InternalVKIDButtonRippleStyle.DARK,
        borderStyle = InternalVKIDButtonBorderStyle.DARK,
        iconStyle = InternalVKIconStyle(
            colorStyle = InternalVKIconColorStyle.BLUE,
            sizeStyle = sizeStyle.internalVKIDAsIconSizeStyle(),
        ),
        textStyle = InternalVKIDButtonTextStyle.DARK,
        progressStyle = InternalCircleProgressStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class TransparentDark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : InternalVKIDButtonStyle(
        backgroundStyle = InternalVKIDButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = InternalVKIDButtonRippleStyle.LIGHT,
        borderStyle = InternalVKIDButtonBorderStyle.LIGHT,
        iconStyle = InternalVKIconStyle(
            colorStyle = InternalVKIconColorStyle.BLUE,
            sizeStyle = sizeStyle.internalVKIDAsIconSizeStyle(),
        ),
        textStyle = InternalVKIDButtonTextStyle.LIGHT,
        progressStyle = InternalCircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )
}
