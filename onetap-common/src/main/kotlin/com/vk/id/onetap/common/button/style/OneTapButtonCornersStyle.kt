package com.vk.id.onetap.common.button.style

public sealed class OneTapButtonCornersStyle(
    public val radiusDp: Int
) {
    public object Default : OneTapButtonCornersStyle(ROUNDED_RADIUS_DP)
    public object None : OneTapButtonCornersStyle(NONE_RADIUS_DP)
    public object Rounded : OneTapButtonCornersStyle(ROUNDED_RADIUS_DP)
    public object Round : OneTapButtonCornersStyle(ROUND_RADIUS_DP)
    public class Custom(radiusDp: Int) : OneTapButtonCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8
        private const val NONE_RADIUS_DP = 0
        private const val ROUND_RADIUS_DP = 1000
    }
}
