package com.vk.id.sample.app.screen.groupsubscription

import androidx.compose.foundation.layout.Box
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
    Box(contentAlignment = Alignment.Center) {
        GroupSubscriptionSheet(
            state = state,
            accessTokenProvider = {
                VKID.instance.accessToken?.token ?: run {
                    showToast(context, "Not authorized")
                    ""
                }
            },
            groupId = "1",
            onSuccess = { showToast(context, "Success") },
            onFail = { showToast(context, "Fail: ${it.description}") },
        )
        Button("Show") { state.show() }
    }
}
