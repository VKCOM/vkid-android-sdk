package com.vk.id.sample.xml.onetap

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.setPadding
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.xml.OneTap
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.onetap.data.buttonStylingData
import com.vk.id.sample.xml.onetap.item.OneTapItem
import com.vk.id.sample.xml.uikit.common.dpToPixels
import com.vk.id.sample.xml.uikit.common.onVKIDAuthFail
import com.vk.id.sample.xml.uikit.common.onVKIDAuthSuccess
import com.vk.id.sample.xml.uikit.item.TitleItem
import com.vk.id.sample.xml.uikit.item.TitleItemView

private const val BUTTON_PADDING = 12
private const val HORIZONTAL_PADDING = 8

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
                                    is OneTapItem -> createOneTap(
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

private fun createOneTap(
    context: Context,
    style: OneTapStyle,
    width: Int,
    isDarkBackground: Boolean,
) = ConstraintLayout(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    setPadding(context.dpToPixels(HORIZONTAL_PADDING), 0, context.dpToPixels(HORIZONTAL_PADDING), 0)
    if (isDarkBackground) setBackgroundResource(R.color.vkid_gray900)
    addView(
        OneTap(context).apply {
            val layoutParams = if (style is OneTapStyle.Icon) {
                LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                )
            } else {
                LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                )
            }
            layoutParams.matchConstraintMaxWidth = context.dpToPixels(width)
            layoutParams.bottomToBottom = ConstraintSet.PARENT_ID
            layoutParams.endToEnd = ConstraintSet.PARENT_ID
            layoutParams.startToStart = ConstraintSet.PARENT_ID
            layoutParams.topToTop = ConstraintSet.PARENT_ID
            setPadding(context.dpToPixels(BUTTON_PADDING))
            this.style = style
            this.layoutParams = layoutParams
            this.isSignInToAnotherAccountEnabled = true
            setCallbacks(
                onAuth = { onVKIDAuthSuccess(context, it) },
                onFail = { onVKIDAuthFail(context, it) },
            )
        }
    )
}
