package com.vk.id.onetap.common.button.style

public sealed class OneTapButtonElevationStyle(
    public val elevationDp: Float
) {
    public object Default : OneTapButtonElevationStyle(0f)
    public class Custom(elevationDp: Float) : OneTapButtonElevationStyle(elevationDp)
}
