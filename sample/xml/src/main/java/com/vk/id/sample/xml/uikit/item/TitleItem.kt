package com.vk.id.sample.xml.uikit.item

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setPadding
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.dpToPixels

private const val TEXT_PADDING = 8

public data class TitleItem(
    val text: String
)

public class TitleItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    init {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        setPadding(context.dpToPixels(TEXT_PADDING))
        setTextAppearance(context, R.style.vkid_onetapSampleTilteStyle)
    }

    public companion object {
        public fun create(
            context: Context,
            text: String,
        ): TitleItemView = TitleItemView(context).apply { this.text = text }
    }
}