package com.vk.id.internal.util

import android.os.Build
import android.os.SystemClock

internal fun currentTime(): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        try {
            SystemClock.currentNetworkTimeClock().millis()
        } catch (_: RuntimeException) {
            System.currentTimeMillis()
        }
    } else {
        System.currentTimeMillis()
    }
}
