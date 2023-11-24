package com.vk.id.sample.xml.onetap

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.xml.VKIDButton
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.onetap.data.buttonStylingData
import com.vk.id.sample.xml.onetap.item.ButtonItem
import com.vk.id.sample.xml.uikit.common.dpToPixels
import com.vk.id.sample.xml.uikit.common.onVKIDAuthFail
import com.vk.id.sample.xml.uikit.common.onVKIDAuthSuccess
import com.vk.id.sample.xml.uikit.item.TitleItem
import com.vk.id.sample.xml.uikit.item.TitleItemView

private const val BUTTON_PADDING = 12

public class OnetapStylingXmlCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ScrollView(this).apply {
                addView(
                    LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        buttonStylingData.forEach {
                            (
                                when (it) {
                                    is TitleItem -> TitleItemView.create(context, it.text)
                                    is ButtonItem -> createVKIDButton(
                                        context,
                                        it.style,
                                        it.width,
                                        it.isDarkBackground
                                    )
                                    else -> null
                                }
                                )?.let(::addView)
                        }
                    }
                )
            }
        )
    }
}

private fun createVKIDButton(
    context: Context,
    style: OneTapStyle,
    width: Int,
    isDarkBackground: Boolean,
) = FrameLayout(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    if (isDarkBackground) setBackgroundResource(R.color.vkid_gray900)
    addView(
        VKIDButton(context).apply {
            val layoutParams = LayoutParams(
                context.dpToPixels(width),
                LayoutParams.WRAP_CONTENT,
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
