package com.vk.id.internal.log

import android.util.Log

/**
 * [LogEngine] implementation that logs to Android's Logcat.
 */
internal class AndroidLogcatLogEngine : LogEngine {
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
