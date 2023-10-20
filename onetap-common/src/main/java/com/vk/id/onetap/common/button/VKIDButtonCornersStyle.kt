package com.vk.id.onetap.common.button

public sealed class VKIDButtonCornersStyle(
    public val radiusDp: Int
) {
    public object Default : VKIDButtonCornersStyle(ROUNDED_RADIUS_DP)
    public object None : VKIDButtonCornersStyle(NONE_RADIUS_DP)
    public object Rounded : VKIDButtonCornersStyle(ROUNDED_RADIUS_DP)
    public object Round : VKIDButtonCornersStyle(ROUND_RADIUS_DP)
    public class Custom(radiusDp: Int) : VKIDButtonCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8
        private const val NONE_RADIUS_DP = 0
        private const val ROUND_RADIUS_DP = 1000
    }
}
