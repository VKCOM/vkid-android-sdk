package com.vk.id.onetap.compose.onetap.sheet.content

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle

@Composable
internal fun SheetContentAuthSuccess(
    serviceName: String,
    style: OneTapBottomSheetStyle,
    dismissSheet: () -> Unit,
) {
    SheetContentBox(
        serviceName,
        style,
        dismissSheet,
    ) {
        BasicText(text = "Success")
    }
}
