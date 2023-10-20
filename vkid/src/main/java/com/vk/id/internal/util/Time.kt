package com.vk.id.internal.util

import android.os.Build
import android.os.SystemClock

internal fun currentTime(): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        SystemClock.currentNetworkTimeClock().millis()
    } else {
        System.currentTimeMillis()
    }
}
