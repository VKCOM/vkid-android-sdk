package com.vk.id.group.subscription.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Stable

/**
 * Manages the state of the Group Subscription Bottom Sheet. Should be created with [rememberOneTapBottomSheetStateInternal]
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
     */
    public fun show() {
        showSheet(true)
    }

    /**
     * Hides the bottom sheet.
     */
    public fun hide() {
        showSheet(false)
    }

    /**
     * Returns the visibility state of the group subscription bottom sheet.
     */
    public val isVisible: Boolean
        get() {
            @OptIn(ExperimentalMaterial3Api::class)
            return materialSheetState.isVisible
        }
}
