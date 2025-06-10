package com.vk.id.group.subscription.xml

import android.content.Context
import android.util.DisplayMetrics

internal fun Context.pixelsToDp(
    px: Float
) = px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
