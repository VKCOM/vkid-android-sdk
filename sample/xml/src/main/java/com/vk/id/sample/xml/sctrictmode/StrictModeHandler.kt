package com.vk.id.sample.xml.sctrictmode

import android.annotation.SuppressLint
import android.content.Context
import android.os.StrictMode
import androidx.preference.PreferenceManager
import com.vk.id.sample.xml.R

public object StrictModeHandler {

    private const val KEY_IS_STRICT_MODE_ENABLED = "KEY_IS_STRICT_MODE_ENABLED"

    public fun isStrictModeEnabled(context: Context): Boolean {
        val defaultValueFromLocalProperties = context.resources.getBoolean(R.bool.default_strict_mode_enabled)
        return getPrefs(context).getBoolean(KEY_IS_STRICT_MODE_ENABLED, defaultValueFromLocalProperties)
    }

    @SuppressLint("ApplySharedPref")
    public fun setStrictModeEnabled(context: Context, isEnabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_IS_STRICT_MODE_ENABLED, isEnabled).commit()
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

    private fun getPrefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
}
