package com.vk.id.logger

import android.util.Log
import com.vk.id.common.InternalVKIDApi

/**
 * [LogEngine] implementation that logs to Android's Logcat.
 */
@InternalVKIDApi
public class AndroidLogcatLogEngine : LogEngine {
    override fun log(
        logLevel: LogEngine.LogLevel,
        tag: String,
        message: String,
        throwable: Throwable?
    ) {
        when (logLevel) {
            LogEngine.LogLevel.INFO -> Log.i(tag, message)
            LogEngine.LogLevel.DEBUG -> Log.d(tag, message)
            LogEngine.LogLevel.ERROR -> Log.e(tag, message, throwable)
        }
    }
}
