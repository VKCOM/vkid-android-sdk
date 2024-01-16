package com.vk.id.onetap.common.button.style

public sealed class OneTapButtonCornersStyle(
    public val radiusDp: Float
) {
    public object Default : OneTapButtonCornersStyle(ROUNDED_RADIUS_DP)
    public object None : OneTapButtonCornersStyle(NONE_RADIUS_DP)
    public object Rounded : OneTapButtonCornersStyle(ROUNDED_RADIUS_DP)
    public object Round : OneTapButtonCornersStyle(ROUND_RADIUS_DP)
    public class Custom(radiusDp: Float) : OneTapButtonCornersStyle(radiusDp)

    private companion object {
        private const val ROUNDED_RADIUS_DP = 8f
        private const val NONE_RADIUS_DP = 0f
        private const val ROUND_RADIUS_DP = 1000f
    }
}
