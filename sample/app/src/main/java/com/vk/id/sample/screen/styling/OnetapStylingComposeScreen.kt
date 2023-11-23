package com.vk.id.sample.screen.styling

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.vk.id.sample.screen.styling.data.buttonStylingData
import com.vk.id.sample.screen.styling.item.HandleButtonItem
import com.vk.id.sample.screen.styling.item.HandleSmallButtonItem
import com.vk.id.sample.uikit.item.HandleHalfSpacerItem
import com.vk.id.sample.uikit.item.HandleSpacerItem
import com.vk.id.sample.uikit.item.HandleTitleItem

@Composable
fun OnetapStylingComposeScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttonStylingData.forEach {
            HandleSpacerItem(item = it)
            HandleHalfSpacerItem(item = it)
            HandleTitleItem(item = it)
            HandleButtonItem(context = context, item = it)
            HandleSmallButtonItem(context = context, item = it)
        }
    }
}
