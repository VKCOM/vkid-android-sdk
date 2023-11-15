package com.vk.id.sample.screen.multibranding.item

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.multibranding.OAuth
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.OAuthListWidgetStyle
import com.vk.id.sample.screen.styling.util.formatToken
import com.vk.id.sample.screen.styling.util.showToast
import com.vk.id.sample.uikit.common.darkBackground

data class OAuthListWidgetItem(
    val style: OAuthListWidgetStyle,
    val filter: (OAuth) -> Boolean,
    val isDarkBackground: Boolean = false,
)

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
            modifier = Modifier.width(355.dp),
            style = item.style,
            onAuth = { oAuth, token ->
                showToast(
                    context,
                    "Auth from $oAuth with token ${formatToken(token)}"
                )
            },
            isOAuthAllowed = item.filter
        )
    }
}
