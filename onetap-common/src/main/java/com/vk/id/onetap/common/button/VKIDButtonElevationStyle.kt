package com.vk.id.onetap.common.button

public sealed class VKIDButtonElevationStyle(
    public val elevation: Int
) {
    public object Default : VKIDButtonElevationStyle(0)
    public class Custom(elevation: Int) : VKIDButtonElevationStyle(elevation)
}
