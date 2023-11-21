package com.vk.id.sample.screen.multibranding.item

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.setPadding
import com.vk.id.internal.auth.OAuth
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.OAuthListWidgetStyle
import com.vk.id.multibranding.xml.OAuthListWidget
import com.vk.id.sample.R
import com.vk.id.sample.screen.multibranding.util.getOAuthListCallback
import com.vk.id.sample.screen.styling.util.onVKIDAuthFail
import com.vk.id.sample.uikit.common.darkBackground
import com.vk.id.sample.uikit.common.dpToPixels

private const val WIDGET_PADDING = 12
private const val WIDGET_WIDTH = 355

data class OAuthListWidgetItem(
    val style: OAuthListWidgetStyle,
    val allowedOAuths: Set<OAuth> = OAuth.values().toSet(),
    val width: Int = WIDGET_WIDTH,
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
            modifier = Modifier.width(item.width.dp),
            style = item.style,
            onAuth = getOAuthListCallback(context),
            onFail = { onVKIDAuthFail(context, it) },
            allowedOAuths = item.allowedOAuths
        )
    }
}

internal fun createOAuthListWidgetItem(
    context: Context,
    item: OAuthListWidgetItem,
) = FrameLayout(context).apply {
    layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    if (item.isDarkBackground) setBackgroundResource(R.color.vkid_gray900)
    addView(
        OAuthListWidget(context).apply {
            val layoutParams = FrameLayout.LayoutParams(
                context.dpToPixels(item.width),
                FrameLayout.LayoutParams.WRAP_CONTENT,
            )
            layoutParams.gravity = Gravity.CENTER
            setPadding(context.dpToPixels(WIDGET_PADDING))
            this.style = item.style
            this.layoutParams = layoutParams
            setCallbacks(
                onAuth = getOAuthListCallback(context),
                onFail = { },
            )
            allowedOAuths = item.allowedOAuths
        }
    )
}
