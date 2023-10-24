package com.vk.id.sample.button

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.vk.id.onetap.xml.VKIDButton
import com.vk.id.onetap.xml.VKIDButtonSmall

private const val VK_ID_BUTTON_WIDTH = 355
private const val VK_ID_BUTTON_MARGIN = 24

@Composable
fun OnetapXmlScreen() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            LinearLayout(it).apply {
                orientation = LinearLayout.VERTICAL
                addView(createVKIDButton(context))
                addView(createVKIDButtonSmall(context))
            }
        }
    )
}

private fun createVKIDButton(context: Context) = VKIDButton(context).apply {
    val layoutParams = LayoutParams(context.dpToPixels(VK_ID_BUTTON_WIDTH), LayoutParams.WRAP_CONTENT)
    layoutParams.gravity = Gravity.CENTER
    layoutParams.topMargin = context.dpToPixels(VK_ID_BUTTON_MARGIN)
    layoutParams.bottomMargin = context.dpToPixels(VK_ID_BUTTON_MARGIN)
    this.layoutParams = layoutParams
}

private fun createVKIDButtonSmall(context: Context) = VKIDButtonSmall(context).apply {
    val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    layoutParams.gravity = Gravity.CENTER
    layoutParams.topMargin = context.dpToPixels(VK_ID_BUTTON_MARGIN)
    layoutParams.bottomMargin = context.dpToPixels(VK_ID_BUTTON_MARGIN)
    this.layoutParams = layoutParams
}

private fun Context.dpToPixels(dp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
}
