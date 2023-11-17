package com.vk.id.onetap.compose.button

import com.vk.id.multibranding.WidgetBorderStyle
import com.vk.id.multibranding.WidgetCornersStyle
import com.vk.id.multibranding.WidgetRippleStyle
import com.vk.id.multibranding.WidgetSizeStyle
import com.vk.id.multibranding.WidgetTextStyle
import com.vk.id.onetap.compose.icon.VKIconColorStyle
import com.vk.id.onetap.compose.icon.VKIconStyle
import com.vk.id.onetap.compose.icon.asIconSizeStyle
import com.vk.id.onetap.compose.progress.CircleProgressStyle

@Suppress("LongParameterList")
public sealed class VKIDButtonStyle(
    public val backgroundStyle: VKIDButtonBackgroundStyle,
    public val rippleStyle: WidgetRippleStyle,
    public val borderStyle: WidgetBorderStyle,
    public val iconStyle: VKIconStyle,
    public val textStyle: WidgetTextStyle,
    public val progressStyle: CircleProgressStyle,
    public val cornersStyle: WidgetCornersStyle,
    public val sizeStyle: WidgetSizeStyle,
    public val elevationStyle: VKIDButtonElevationStyle,
) {
    public class Blue(
        cornersStyle: WidgetCornersStyle = WidgetCornersStyle.Default,
        sizeStyle: WidgetSizeStyle = WidgetSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.BLUE,
        rippleStyle = WidgetRippleStyle.LIGHT,
        borderStyle = WidgetBorderStyle.NONE,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.WHITE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = WidgetTextStyle.LIGHT,
        progressStyle = CircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class TransparentDark(
        cornersStyle: WidgetCornersStyle = WidgetCornersStyle.Default,
        sizeStyle: WidgetSizeStyle = WidgetSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = WidgetRippleStyle.DARK,
        borderStyle = WidgetBorderStyle.DARK,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.BLUE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = WidgetTextStyle.DARK,
        progressStyle = CircleProgressStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class TransparentLight(
        cornersStyle: WidgetCornersStyle = WidgetCornersStyle.Default,
        sizeStyle: WidgetSizeStyle = WidgetSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = WidgetRippleStyle.LIGHT,
        borderStyle = WidgetBorderStyle.LIGHT,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.BLUE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = WidgetTextStyle.LIGHT,
        progressStyle = CircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )
}
