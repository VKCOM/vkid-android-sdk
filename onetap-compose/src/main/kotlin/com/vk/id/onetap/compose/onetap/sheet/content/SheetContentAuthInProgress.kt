package com.vk.id.onetap.compose.onetap.sheet.content

import androidx.compose.runtime.Composable
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle

@Composable
internal fun SheetContentAuthInProgress(serviceName: String, style: OneTapBottomSheetStyle, dismissSheet: () -> Unit) {
    SheetContentBox(
        serviceName,
        style,
        dismissSheet,
    ) {
        // Progress
    }
}
