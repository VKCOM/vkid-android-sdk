package com.vk.id.sample.app.screen.groupsubscription

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.vk.id.VKID
import com.vk.id.group.subscription.compose.GroupSubscriptionSheet
import com.vk.id.group.subscription.compose.rememberGroupSubscriptionSheetState
import com.vk.id.sample.app.screen.Button
import com.vk.id.sample.xml.uikit.common.showToast

@Composable
internal fun GroupSubscriptionScreen() {
    val context = LocalContext.current
    val state = rememberGroupSubscriptionSheetState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        GroupSubscriptionSheet(
            state = state,
            accessToken = VKID.instance.accessToken?.token ?: run {
                showToast(context, "Not authorized")
                ""
            },
            groupId = "1",
            onSuccess = { showToast(context, "Success") },
            onCancel = { showToast(context, "Cancelled") },
        )
        Button("Show") { state.show() }
    }
}
