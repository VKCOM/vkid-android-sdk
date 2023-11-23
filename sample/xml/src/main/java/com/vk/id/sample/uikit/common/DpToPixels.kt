package com.vk.id.sample.uikit.common

import android.content.Context
import android.util.TypedValue

public fun Context.dpToPixels(dp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
}
