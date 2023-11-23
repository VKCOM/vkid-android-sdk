package com.vk.id.onetap.compose.onetap

import com.vk.id.onetap.compose.button.alternate.style.AlternateAccountButtonStyle
import com.vk.id.onetap.compose.button.auth.style.VKIDButtonStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonCornersStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonElevationStyle
import com.vk.id.onetap.compose.onetap.style.OneTapButtonSizeStyle

public sealed class OneTapStyle(
    public val cornersStyle: OneTapButtonCornersStyle,
    public val sizeStyle: OneTapButtonSizeStyle,
    public val elevationStyle: OneTapButtonElevationStyle,
    internal val vkidButtonStyle: VKIDButtonStyle,
    internal val alternateAccountButtonStyle: AlternateAccountButtonStyle
) {
    public class Light(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.Blue(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Light(cornersStyle, sizeStyle)
    )

    public class Dark(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.Blue(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Dark(cornersStyle, sizeStyle)
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
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Dark(cornersStyle, sizeStyle)
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
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Light(cornersStyle, sizeStyle)
    )

    public class Icon(
        cornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        sizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
        elevationStyle: OneTapButtonElevationStyle = OneTapButtonElevationStyle.Default,
    ) : OneTapStyle(
        cornersStyle = cornersStyle,
        sizeStyle = sizeStyle,
        elevationStyle = elevationStyle,
        vkidButtonStyle = VKIDButtonStyle.Blue(cornersStyle, sizeStyle, elevationStyle),
        alternateAccountButtonStyle = AlternateAccountButtonStyle.Light(cornersStyle, sizeStyle)
    )
}
