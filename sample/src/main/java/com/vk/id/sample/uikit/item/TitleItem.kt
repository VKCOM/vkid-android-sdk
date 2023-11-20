package com.vk.id.sample.uikit.item

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.setPadding
import com.vk.id.sample.R
import com.vk.id.sample.uikit.common.dpToPixels

private const val TEXT_PADDING = 8

data class TitleItem(
    val text: String
)

@Composable
fun HandleTitleItem(item: Any) {
    if (item !is TitleItem) return
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(all = 8.dp),
            text = item.text,
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

fun createTitleItem(
    context: Context,
    text: String,
) = TextView(context).apply {
    layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    this.text = text
    setPadding(context.dpToPixels(TEXT_PADDING))
    setTextAppearance(context, R.style.onetapSampleTilteStyle)
}
