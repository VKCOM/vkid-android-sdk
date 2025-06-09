package com.vk.id.sample.xml.prefs

import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.PreferenceManager

internal class StringPrefsHandle(
    private val key: String
) {
    fun get(context: Context, defaultValue: String?): String? {
        return getPrefs(context).getString(key, defaultValue)
    }

    @SuppressLint("ApplySharedPref")
    fun set(context: Context, value: String?) {
        getPrefs(context).edit().putString(key, value).commit()
    }

    private fun getPrefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
}
