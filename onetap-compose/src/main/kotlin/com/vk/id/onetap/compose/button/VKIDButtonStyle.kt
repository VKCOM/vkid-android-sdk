package com.vk.id.onetap.compose.button

import com.vk.id.multibranding.BorderStyle
import com.vk.id.multibranding.CornersStyle
import com.vk.id.multibranding.RippleStyle
import com.vk.id.multibranding.TextStyle
import com.vk.id.onetap.compose.icon.VKIconColorStyle
import com.vk.id.onetap.compose.icon.VKIconStyle
import com.vk.id.onetap.compose.icon.asIconSizeStyle
import com.vk.id.onetap.compose.progress.CircleProgressStyle

@Suppress("LongParameterList")
public sealed class VKIDButtonStyle(
    public val backgroundStyle: VKIDButtonBackgroundStyle,
    public val rippleStyle: RippleStyle,
    public val borderStyle: BorderStyle,
    public val iconStyle: VKIconStyle,
    public val textStyle: TextStyle,
    public val progressStyle: CircleProgressStyle,
    public val cornersStyle: CornersStyle,
    public val sizeStyle: VKIDButtonSizeStyle,
    public val elevationStyle: VKIDButtonElevationStyle,
) {
    public class Blue(
        cornersStyle: CornersStyle = CornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.BLUE,
        rippleStyle = RippleStyle.LIGHT,
        borderStyle = BorderStyle.NONE,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.WHITE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = TextStyle.LIGHT,
        progressStyle = CircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class TransparentDark(
        cornersStyle: CornersStyle = CornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = RippleStyle.DARK,
        borderStyle = BorderStyle.DARK,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.BLUE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = TextStyle.DARK,
        progressStyle = CircleProgressStyle.DARK,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )

    public class TransparentLight(
        cornersStyle: CornersStyle = CornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : VKIDButtonStyle(
        backgroundStyle = VKIDButtonBackgroundStyle.TRANSPARENT,
        rippleStyle = RippleStyle.LIGHT,
        borderStyle = BorderStyle.LIGHT,
        iconStyle = VKIconStyle(
            colorStyle = VKIconColorStyle.BLUE,
            sizeStyle = sizeStyle.asIconSizeStyle(),
        ),
        textStyle = TextStyle.LIGHT,
        progressStyle = CircleProgressStyle.LIGHT,
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
    )
}
