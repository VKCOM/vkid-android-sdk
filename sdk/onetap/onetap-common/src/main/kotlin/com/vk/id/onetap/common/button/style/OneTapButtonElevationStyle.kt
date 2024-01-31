package com.vk.id.onetap.common.button.style

/**
 * OneTap's button elevation style.
 */
public sealed class OneTapButtonElevationStyle(
    /**
     * Button elevation in DPs.
     */
    public val elevationDp: Float
) {
    /**
     * The recommended style.
     */
    public object Default : OneTapButtonElevationStyle(0f)

    /**
     * A custom style with required elevation.
     *
     * @param elevationDp Elevation in DPs.
     */
    public class Custom(elevationDp: Float) : OneTapButtonElevationStyle(elevationDp)
}
