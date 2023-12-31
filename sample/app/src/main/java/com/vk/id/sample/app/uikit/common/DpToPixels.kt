package com.vk.id.sample.app.uikit.common

import android.content.Context
import android.util.TypedValue

internal fun Context.dpToPixels(dp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
}
