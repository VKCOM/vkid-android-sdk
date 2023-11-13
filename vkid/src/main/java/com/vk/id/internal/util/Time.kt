package com.vk.id.internal.util

import android.os.Build
import android.os.SystemClock
import com.vk.id.internal.log.VKIDLog

@Suppress("TooGenericExceptionCaught")
internal fun currentTime(): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        try {
            SystemClock.currentNetworkTimeClock().millis()
        } catch (exception: RuntimeException) {
            VKIDLog.createLoggerForTag("com.vk.id.internal.util.currentTime")
                .error("Most likely device was started without access to internet", exception)
            System.currentTimeMillis()
        }
    } else {
        System.currentTimeMillis()
    }
}
