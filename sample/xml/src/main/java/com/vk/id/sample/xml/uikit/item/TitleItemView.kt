package com.vk.id.sample.xml.uikit.item

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.dpToPixels
import com.vk.id.sample.xml.uikit.common.isDarkTheme

private const val TEXT_PADDING = 8
private const val TEXT_FONT_WEIGHT = 700

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
        setTextAppearance(context, R.style.vkid_onetapSampleTitleStyle)
        val colorRes = if (context.isDarkTheme) R.color.vkid_white else R.color.vkid_gray900
        setTextColor(ContextCompat.getColor(context, colorRes))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            typeface = Typeface.create(null, TEXT_FONT_WEIGHT, false)
        }
    }

    public companion object {
        public fun create(
            context: Context,
            text: String,
        ): TitleItemView = TitleItemView(context).apply { this.text = text }
    }
}
