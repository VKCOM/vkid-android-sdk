package com.vk.id.sample.xml.prefs.flutter

import android.annotation.SuppressLint
import android.content.Context
import com.vk.id.sample.xml.prefs.BooleanPrefsHandle

public object IsFlutterHandler {

    private val handle = BooleanPrefsHandle("KEY_IS_FLUTTER")

    public fun isFlutter(context: Context): Boolean {
        return handle.get(context, false)
    }

    @SuppressLint("ApplySharedPref")
    public fun setIsFlutter(context: Context, isFlutter: Boolean) {
        return handle.set(context, isFlutter)
    }
}
