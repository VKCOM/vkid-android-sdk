package com.vk.id.sample.xml.flutter

import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.PreferenceManager

public object IsFlutterHandler {

    private const val KEY_IS_FLUTTER = "KEY_IS_FLUTTER"

    public fun isFlutter(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_IS_FLUTTER, false)
    }

    @SuppressLint("ApplySharedPref")
    public fun setIsFlutter(context: Context, isFlutter: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_IS_FLUTTER, isFlutter).commit()
    }

    private fun getPrefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
}
