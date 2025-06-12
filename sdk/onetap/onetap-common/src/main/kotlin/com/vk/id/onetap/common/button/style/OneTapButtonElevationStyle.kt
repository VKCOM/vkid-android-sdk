package com.vk.id.onetap.common.button.style

import androidx.compose.runtime.Immutable

/**
 * OneTap's button elevation style.
 *
 * @since 1.0.0
 */
@Immutable
public sealed class OneTapButtonElevationStyle(
    /**
     * Button elevation in DPs.
     *
     * @since 1.0.0
     */
    public val elevationDp: Float
) {
    /**
     * The recommended style.
     *
     * @since 1.0.0
     */
    @Suppress("ConvertObjectToDataObject")
    public object Default : OneTapButtonElevationStyle(0f)

    /**
     * A custom style with required elevation.
     *
     * @param elevationDp Elevation in DPs.
     *
     * @since 1.0.0
     */
    public class Custom(elevationDp: Float) : OneTapButtonElevationStyle(elevationDp)
}
