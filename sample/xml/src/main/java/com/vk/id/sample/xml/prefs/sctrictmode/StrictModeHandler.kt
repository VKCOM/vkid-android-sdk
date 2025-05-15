package com.vk.id.sample.xml.prefs.sctrictmode

import android.annotation.SuppressLint
import android.content.Context
import android.os.StrictMode
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.prefs.BooleanPrefsHandle

public object StrictModeHandler {

    private val handle = BooleanPrefsHandle("KEY_IS_STRICT_MODE_ENABLED")

    public fun isStrictModeEnabled(context: Context): Boolean {
        val defaultValueFromLocalProperties = context.resources.getBoolean(R.bool.default_strict_mode_enabled)
        return handle.get(context, defaultValueFromLocalProperties)
    }

    @SuppressLint("ApplySharedPref")
    public fun setStrictModeEnabled(context: Context, isEnabled: Boolean) {
        return handle.set(context, isEnabled)
    }

    internal fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build()
        )
    }
}
