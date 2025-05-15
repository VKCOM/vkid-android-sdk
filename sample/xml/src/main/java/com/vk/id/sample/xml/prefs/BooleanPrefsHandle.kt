package com.vk.id.sample.xml.prefs

import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.PreferenceManager

internal class BooleanPrefsHandle(
    private val key: String
) {
    fun get(context: Context, defaultValue: Boolean): Boolean {
        return getPrefs(context).getBoolean(key, defaultValue)
    }

    @SuppressLint("ApplySharedPref")
    fun set(context: Context, isEnabled: Boolean) {
        getPrefs(context).edit().putBoolean(key, isEnabled).commit()
    }

    private fun getPrefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
}
