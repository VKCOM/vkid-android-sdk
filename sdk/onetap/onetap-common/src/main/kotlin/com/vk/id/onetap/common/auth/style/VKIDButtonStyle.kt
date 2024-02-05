package com.vk.id.onetap.common.auth.style

import androidx.compose.runtime.Immutable
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.common.button.style.asIconSizeStyle
import com.vk.id.onetap.common.icon.style.VKIconColorStyle
import com.vk.id.onetap.common.icon.style.VKIconStyle
import com.vk.id.onetap.common.progress.style.CircleProgressStyle

@Suppress("LongParameterList")
@Immutable
@InternalVKIDApi
public sealed class VKIDButtonStyle(
    public val backgroundStyle: VKIDButtonBackgroundStyle,
    public val rippleStyle: VKIDButtonRippleStyle,
    public val borderStyle: VKIDButtonBorderStyle,
    public val iconStyle: VKIconStyle,
    public val textStyle: VKIDButtonTextStyle,
    public val progressStyle: CircleProgressStyle,
    public val cornersStyle: OneTapButtonCornersStyle,
    public val sizeStyle: OneTapButtonSizeStyle,
    public val elevationStyle: OneTapButtonElevationStyle,
) {
    public class Light(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.BLUE,
        rippleStyle = VKIDButtonRippleStyle.LIGHT,
        borderStyle = VKIDButtonBorderStyle.NONE,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.WHITE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = VKIDButtonTextStyle.LIGHT,
        progressStyle = CircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class Dark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.BLUE,
        rippleStyle = VKIDButtonRippleStyle.LIGHT,
        borderStyle = VKIDButtonBorderStyle.NONE,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.WHITE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = VKIDButtonTextStyle.LIGHT,
        progressStyle = CircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class TransparentLight(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = VKIDButtonRippleStyle.DARK,
        borderStyle = VKIDButtonBorderStyle.DARK,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.BLUE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = VKIDButtonTextStyle.DARK,
        progressStyle = CircleProgressStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class TransparentDark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.MEDIUM_44,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = VKIDButtonRippleStyle.LIGHT,
        borderStyle = VKIDButtonBorderStyle.LIGHT,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.BLUE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = VKIDButtonTextStyle.LIGHT,
        progressStyle = CircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )
}
