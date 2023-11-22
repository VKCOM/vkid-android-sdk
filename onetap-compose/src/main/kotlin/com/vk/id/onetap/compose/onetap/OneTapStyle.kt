package com.vk.id.onetap.compose.onetap

import com.vk.id.onetap.compose.button.VKIDButtonCornersStyle
import com.vk.id.onetap.compose.button.VKIDButtonElevationStyle
import com.vk.id.onetap.compose.button.VKIDButtonSizeStyle
import com.vk.id.onetap.compose.button.VKIDButtonStyle
import com.vk.id.onetap.compose.button.alternate.style.AlternateAccountButtonStyle

public sealed class OneTapStyle(
    public val cornersStyle: VKIDButtonCornersStyle,
    public val sizeStyle: VKIDButtonSizeStyle,
    public val elevationStyle: VKIDButtonElevationStyle,
    internal val vkidButtonStyle: VKIDButtonStyle,
    internal val alternateAccountButtonStyle: AlternateAccountButtonStyle
) {
    public class Light(
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.DEFAULT,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.Blue(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Light(cornersStyle, sizeStyle)
    )

    public class Dark(
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.DEFAULT,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.Blue(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Dark(cornersStyle, sizeStyle)
    )

    public class TransparentLight(
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.DEFAULT,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.TransparentLight(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Dark(cornersStyle, sizeStyle)
    )

    public class TransparentDark(
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.DEFAULT,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.TransparentDark(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Light(cornersStyle, sizeStyle)
    )

    public class Icon(
        cornersStyle: VKIDButtonCornersStyle = VKIDButtonCornersStyle.Default,
        sizeStyle: VKIDButtonSizeStyle = VKIDButtonSizeStyle.DEFAULT,
        elevationStyle: VKIDButtonElevationStyle = VKIDButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.Blue(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Light(cornersStyle, sizeStyle)
    )
}

// TODO Временно пока нет OneTap в xml
public fun OneTapStyle.buttonStyle(): VKIDButtonStyle {
    return this.vkidButtonStyle
}
