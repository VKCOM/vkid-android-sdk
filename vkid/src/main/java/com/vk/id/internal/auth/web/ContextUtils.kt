package com.vk.id.internal.auth.web

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent

internal object ContextUtils {
    fun Intent.addNewTaskFlag(context: Context): Intent {
        if (context.toActivitySafe() == null) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return this
    }

    private fun Context.toActivitySafe(): Activity? {
        var context = this
        while (context !is Activity && context is ContextWrapper) context = context.baseContext
        return if (context is Activity) context else null
    }
}
