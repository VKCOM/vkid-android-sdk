package com.vk.id.sample.app.screen.multibranding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.sample.app.screen.multibranding.item.HandleOAuthListWidgetItem
import com.vk.id.sample.app.uikit.item.HandleHalfSpacerItem
import com.vk.id.sample.app.uikit.item.HandleSpacerItem
import com.vk.id.sample.app.uikit.item.HandleTitleItem
import com.vk.id.sample.xml.multibranding.data.multibrandingSampleData

@Preview
@Composable
fun MultibrandingComposeScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        multibrandingSampleData.forEach {
            HandleSpacerItem(item = it)
            HandleHalfSpacerItem(item = it)
            HandleTitleItem(item = it)
            HandleOAuthListWidgetItem(context = context, item = it)
        }
    }
}
