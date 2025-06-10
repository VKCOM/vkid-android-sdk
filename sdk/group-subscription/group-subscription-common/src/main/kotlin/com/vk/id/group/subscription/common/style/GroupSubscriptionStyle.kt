@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.common.style

import android.content.Context
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.util.internalVkIdIsDarkTheme

/**
 * Base class for defining the style of the Group Subscription Sheet.
 *
 * @param cornersStyle The style for sheet corners.
 * @param buttonsCornersStyle The style for button corners.
 * @param buttonsSizeStyle The style for button size.
 *
 * @since 2.5.0
 */
public sealed class GroupSubscriptionStyle @InternalVKIDApi constructor(
    /**
     * The style for sheet corners.
     *
     * @since 2.5.0
     */
    public val cornersStyle: GroupSubscriptionSheetCornersStyle = GroupSubscriptionSheetCornersStyle.Default,
    /**
     * The style for button corners.
     *
     * @since 2.5.0
     */
    public val buttonsCornersStyle: GroupSubscriptionButtonsCornersStyle = GroupSubscriptionButtonsCornersStyle.Default,
    /**
     * The style for button size.
     *
     * @since 2.5.0
     */
    public val buttonsSizeStyle: GroupSubscriptionButtonsSizeStyle = GroupSubscriptionButtonsSizeStyle.DEFAULT,
) {
    @InternalVKIDApi public val isLight: Boolean = this is Light

    /**
     * Represents the light style theme for the Group Subscription Sheet.
     *
     * @param cornersStyle The style for sheet corners.
     * @param buttonsCornersStyle The style for button corners.
     * @param buttonsSizeStyle The style for button size.
     *
     * @since 2.5.0
     */
    public class Light(
        cornersStyle: GroupSubscriptionSheetCornersStyle = GroupSubscriptionSheetCornersStyle.Default,
        buttonsCornersStyle: GroupSubscriptionButtonsCornersStyle = GroupSubscriptionButtonsCornersStyle.Default,
        buttonsSizeStyle: GroupSubscriptionButtonsSizeStyle = GroupSubscriptionButtonsSizeStyle.DEFAULT,
    ) : GroupSubscriptionStyle(
        cornersStyle = cornersStyle,
        buttonsCornersStyle = buttonsCornersStyle,
        buttonsSizeStyle = buttonsSizeStyle,
    )

    /**
     * Represents the dark style theme for the Group Subscription Sheet.
     *
     * @param cornersStyle The style for sheet corners.
     * @param buttonsCornersStyle The style for button corners.
     * @param buttonsSizeStyle The style for button size.
     *
     * @since 2.5.0
     */
    public class Dark(
        cornersStyle: GroupSubscriptionSheetCornersStyle = GroupSubscriptionSheetCornersStyle.Default,
        buttonsCornersStyle: GroupSubscriptionButtonsCornersStyle = GroupSubscriptionButtonsCornersStyle.Default,
        buttonsSizeStyle: GroupSubscriptionButtonsSizeStyle = GroupSubscriptionButtonsSizeStyle.DEFAULT,
    ) : GroupSubscriptionStyle(
        cornersStyle = cornersStyle,
        buttonsCornersStyle = buttonsCornersStyle,
        buttonsSizeStyle = buttonsSizeStyle,
    )

    /** @suppress */
    public companion object {
        /**
         * Create a style for the Group Subscription Bottomsheet that
         * varies between [Light] and [Dark] based on system settings.
         *
         * @param context The [Context] that can be used to retrieve system theme.
         * @param cornersStyle The style for sheet corners.
         * @param buttonsCornersStyle The style for button corners.
         * @param buttonsSizeStyle The style for button size.
         *
         * @since 2.5.0
         */
        public fun system(
            context: Context,
            cornersStyle: GroupSubscriptionSheetCornersStyle = GroupSubscriptionSheetCornersStyle.Default,
            buttonsCornersStyle: GroupSubscriptionButtonsCornersStyle = GroupSubscriptionButtonsCornersStyle.Default,
            buttonsSizeStyle: GroupSubscriptionButtonsSizeStyle = GroupSubscriptionButtonsSizeStyle.DEFAULT,
        ): GroupSubscriptionStyle {
            return (if (context.internalVkIdIsDarkTheme) ::Dark else ::Light)(
                cornersStyle,
                buttonsCornersStyle,
                buttonsSizeStyle,
            )
        }
    }
}
