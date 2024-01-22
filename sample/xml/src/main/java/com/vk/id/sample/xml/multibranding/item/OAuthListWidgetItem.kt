package com.vk.id.sample.xml.multibranding.item

import android.content.Context
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.setPadding
import com.vk.id.OAuth
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle
import com.vk.id.multibranding.xml.OAuthListWidget
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.dpToPixels
import com.vk.id.sample.xml.uikit.common.getMultibrandingFailCallback
import com.vk.id.sample.xml.uikit.common.getMultibrandingSuccessCallback

private const val WIDGET_PADDING = 8
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
) = ConstraintLayout(context).apply {
    layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    if (item.isDarkBackground) setBackgroundResource(R.color.vkid_gray900)
    addView(
        OAuthListWidget(context).apply {
            val layoutParams = ConstraintLayout.LayoutParams(
                0,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
            )
            layoutParams.matchConstraintMaxWidth = context.dpToPixels(item.width)
            layoutParams.bottomToBottom = ConstraintSet.PARENT_ID
            layoutParams.endToEnd = ConstraintSet.PARENT_ID
            layoutParams.startToStart = ConstraintSet.PARENT_ID
            layoutParams.topToTop = ConstraintSet.PARENT_ID
            setPadding(context.dpToPixels(WIDGET_PADDING))
            this.style = item.style
            this.layoutParams = layoutParams
            setCallbacks(
                onAuth = getMultibrandingSuccessCallback(context) {},
                onFail = getMultibrandingFailCallback(context),
            )
            oAuths = item.oAuths
        }
    )
}
