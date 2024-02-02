package com.vk.id.onetap.common.button.style

import androidx.compose.runtime.Immutable

/**
 * OneTap's button elevation style.
 */
@Immutable
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
