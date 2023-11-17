package com.vk.id.sample.screen.multibranding

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.sample.screen.multibranding.data.multibrandingSampleData
import com.vk.id.sample.screen.multibranding.item.HandleOAuthListWidgetItem
import com.vk.id.sample.uikit.spacer.HandleHalfSpacerItem
import com.vk.id.sample.uikit.spacer.HandleSpacerItem
import com.vk.id.sample.uikit.spacer.HandleTitleItem

@Preview
@Composable
fun MultibrandingComposeScreen() {
    val context = LocalContext.current
    Column {
        multibrandingSampleData.forEach {
            HandleSpacerItem(item = it)
            HandleHalfSpacerItem(item = it)
            HandleTitleItem(item = it)
            HandleOAuthListWidgetItem(context = context, item = it)
        }
    }
}
