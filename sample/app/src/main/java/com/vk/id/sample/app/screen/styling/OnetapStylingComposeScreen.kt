package com.vk.id.sample.app.screen.styling

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.vk.id.AccessToken
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.app.screen.styling.item.HandleButtonItem
import com.vk.id.sample.app.uikit.item.HandleHalfSpacerItem
import com.vk.id.sample.app.uikit.item.HandleSpacerItem
import com.vk.id.sample.app.uikit.item.HandleTitleItem
import com.vk.id.sample.xml.onetap.data.buttonStylingData

@Composable
fun OnetapStylingComposeScreen() {
    val token: MutableState<AccessToken?> = remember { mutableStateOf(null) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        token.value?.let {
            UseToken(accessToken = it)
        }
        buttonStylingData.forEach {
            HandleSpacerItem(item = it)
            HandleHalfSpacerItem(item = it)
            HandleTitleItem(item = it)
            HandleButtonItem(context = context, item = it) {
                token.value = it
            }
        }
    }
}
