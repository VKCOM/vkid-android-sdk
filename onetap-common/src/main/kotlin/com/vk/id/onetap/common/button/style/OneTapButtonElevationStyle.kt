package com.vk.id.onetap.common.button.style

public sealed class OneTapButtonElevationStyle(
    public val elevation: Int
) {
    public object Default : OneTapButtonElevationStyle(0)
    public class Custom(elevation: Int) : OneTapButtonElevationStyle(elevation)
}
