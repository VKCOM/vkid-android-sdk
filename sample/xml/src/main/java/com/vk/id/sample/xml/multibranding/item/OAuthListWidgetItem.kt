package com.vk.id.sample.xml.multibranding.item

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.vk.id.OAuth
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle
import com.vk.id.multibranding.xml.OAuthListWidget
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.multibranding.util.getOAuthListCallback
import com.vk.id.sample.xml.uikit.common.dpToPixels
import com.vk.id.sample.xml.uikit.common.onVKIDAuthFail

private const val WIDGET_PADDING = 12
private const val WIDGET_WIDTH = 355

public data class OAuthListWidgetItem(
    val style: OAuthListWidgetStyle,
    val oAuths: Set<OAuth> = OAuth.values().toSet(),
    val width: Int = WIDGET_WIDTH,
    val isDarkBackground: Boolean = false,
)

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
                onFail = { onVKIDAuthFail(context, it) },
            )
            oAuths = item.oAuths
        }
    )
}
