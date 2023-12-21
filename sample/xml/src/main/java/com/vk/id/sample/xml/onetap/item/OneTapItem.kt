package com.vk.id.sample.xml.onetap.item

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.setPadding
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.xml.OneTap
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.dpToPixels
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback

private const val BUTTON_PADDING = 12
private const val HORIZONTAL_PADDING = 8

public data class OneTapItem(
    val style: OneTapStyle,
    val width: Int = 335,
    val isDarkBackground: Boolean = false,
    val oAuths: Set<OneTapOAuth> = emptySet(),
)

public fun createOneTap(
    context: Context,
    item: OneTapItem,
): ConstraintLayout = ConstraintLayout(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    setPadding(context.dpToPixels(HORIZONTAL_PADDING), 0, context.dpToPixels(HORIZONTAL_PADDING), 0)
    if (item.isDarkBackground) setBackgroundResource(R.color.vkid_gray900)
    addView(
        OneTap(context).apply {
            val layoutParams = if (item.style is OneTapStyle.Icon) {
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
            this.oAuths = item.oAuths
            this.isSignInToAnotherAccountEnabled = true
            setCallbacks(
                onAuth = getOneTapSuccessCallback(context) {},
                onFail = getOneTapFailCallback(context),
            )
        }
    )
}
