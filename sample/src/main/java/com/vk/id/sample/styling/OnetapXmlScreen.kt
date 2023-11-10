package com.vk.id.sample.styling

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.setPadding
import com.vk.id.onetap.compose.button.VKIDButtonStyle
import com.vk.id.onetap.xml.VKIDButton
import com.vk.id.onetap.xml.VKIDButtonSmall
import com.vk.id.sample.R

private const val BUTTON_PADDING = 12
private const val TEXT_PADDING = 8

@Composable
fun OnetapXmlScreen() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            ScrollView(context).apply {
                addView(
                    LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        buttonStylingData.forEach {
                            when (it) {
                                is ListItem.Spacer -> null
                                is ListItem.HalfSpacer -> null
                                is ListItem.Title -> createText(context, it.text)
                                is ListItem.Button -> createVKIDButton(context, it.style, it.width, it.isDarkBackground)
                                is ListItem.SmallButton -> createVKIDButtonSmall(context, it.style)
                            }?.let(::addView)
                        }
                    }
                )
            }
        }
    )
}

private fun createText(
    context: Context,
    text: String,
) = TextView(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    this.text = text
    setPadding(context.dpToPixels(TEXT_PADDING))
    setTextAppearance(context, R.style.onetapSampleTilteStyle)
}

private fun createVKIDButton(
    context: Context,
    style: VKIDButtonStyle,
    width: Int,
    isDarkBackground: Boolean,
) = FrameLayout(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    if (isDarkBackground) setBackgroundResource(R.color.vkid_gray900)
    addView(
        VKIDButton(context).apply {
            val layoutParams = FrameLayout.LayoutParams(
                context.dpToPixels(width),
                FrameLayout.LayoutParams.WRAP_CONTENT,
            )
            layoutParams.gravity = Gravity.CENTER
            setPadding(context.dpToPixels(BUTTON_PADDING))
            this.style = style
            this.layoutParams = layoutParams
            setCallbacks(
                onAuth = { onVKIDAuthSuccess(context, it) },
                onFail = { onVKIDAuthFail(context, it) },
            )
        }
    )
}

private fun createVKIDButtonSmall(
    context: Context,
    style: VKIDButtonStyle,
) = VKIDButtonSmall(context).apply {
    val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    layoutParams.gravity = Gravity.CENTER
    setPadding(context.dpToPixels(BUTTON_PADDING))
    this.style = style
    this.layoutParams = layoutParams
    setCallbacks(
        onAuth = { onVKIDAuthSuccess(context, it) },
        onFail = { onVKIDAuthFail(context, it) },
    )
}

private fun Context.dpToPixels(dp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
}
