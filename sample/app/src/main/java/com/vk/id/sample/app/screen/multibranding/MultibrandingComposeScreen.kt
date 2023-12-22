package com.vk.id.sample.app.screen.multibranding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.AccessToken
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.app.screen.multibranding.item.HandleOAuthListWidgetItem
import com.vk.id.sample.app.uikit.item.HandleHalfSpacerItem
import com.vk.id.sample.app.uikit.item.HandleSpacerItem
import com.vk.id.sample.app.uikit.item.HandleTitleItem
import com.vk.id.sample.xml.multibranding.data.multibrandingSampleData

@Preview
@Composable
fun MultibrandingComposeScreen() {
    val context = LocalContext.current
    val token: MutableState<AccessToken?> = remember { mutableStateOf(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        token.value?.let {
            Box(Modifier.padding(12.dp)) {
                UseToken(accessToken = it)
            }
        }
        multibrandingSampleData.forEach {
            HandleSpacerItem(item = it)
            HandleHalfSpacerItem(item = it)
            HandleTitleItem(item = it)
            HandleOAuthListWidgetItem(context = context, item = it, token)
        }
    }
}
