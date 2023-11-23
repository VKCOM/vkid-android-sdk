package com.vk.id.sample.uikit.item

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.vk.id.sample.uikit.common.dpToPixels
import com.vk.id.sample.xml.R

private const val TEXT_PADDING = 8

public data class TitleItem(
    val text: String
)

public fun createTitleItem(
    context: Context,
    text: String,
): TextView = TextView(context).apply {
    layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    this.text = text
    setPadding(context.dpToPixels(TEXT_PADDING))
    setTextAppearance(context, R.style.vkid_onetapSampleTilteStyle)
}
