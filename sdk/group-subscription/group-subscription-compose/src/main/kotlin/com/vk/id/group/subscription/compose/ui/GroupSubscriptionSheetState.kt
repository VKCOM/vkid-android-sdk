package com.vk.id.group.subscription.compose.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Stable

/**
 * Manages the state of the Group Subscription Bottom Sheet. Should be created with [rememberOneTapBottomSheetStateInternal]
 *
 * @since 2.5.0
 */
@Stable
public class GroupSubscriptionSheetState
@OptIn(ExperimentalMaterial3Api::class)
internal constructor(
    internal val materialSheetState: SheetState
) {
    internal var showSheet: (Boolean) -> Unit = {}

    /**
     * Shows the bottom sheet.
     *
     * @since 2.5.0
     */
    public fun show() {
        showSheet(true)
    }

    /**
     * Hides the bottom sheet.
     *
     * @since 2.5.0
     */
    public fun hide() {
        showSheet(false)
    }

    /**
     * Returns the visibility state of the group subscription bottom sheet.
     *
     * @since 2.5.0
     */
    public val isVisible: Boolean
        get() {
            @OptIn(ExperimentalMaterial3Api::class)
            return materialSheetState.isVisible
        }
}
