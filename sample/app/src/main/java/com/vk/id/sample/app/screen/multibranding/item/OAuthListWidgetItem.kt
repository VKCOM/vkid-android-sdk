package com.vk.id.sample.app.screen.multibranding.item

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.sample.xml.multibranding.util.getOAuthListCallback
import com.vk.id.sample.app.screen.styling.util.onVKIDAuthFail
import com.vk.id.sample.xml.multibranding.item.OAuthListWidgetItem
import com.vk.id.sample.app.uikit.common.darkBackground

@Composable
fun HandleOAuthListWidgetItem(
    context: Context,
    item: Any
) {
    if (item !is OAuthListWidgetItem) return
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .darkBackground(item.isDarkBackground)
            .fillMaxWidth()
    ) {
        OAuthListWidget(
            modifier = Modifier.width(item.width.dp),
            style = item.style,
            onAuth = getOAuthListCallback(context),
            onFail = { onVKIDAuthFail(context, it) },
            allowedOAuths = item.allowedOAuths
        )
    }
}
