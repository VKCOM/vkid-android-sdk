package com.vk.id.analytics.stat

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import java.util.UUID

internal class AnalyticsStorage(
    context: Context
) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    var externalDeviceId: String
        get() {
            return prefs.getString(KEY_EXTERNAL_DEVICE_ID, null) ?: run {
                UUID.randomUUID().toString().also { externalDeviceId = it }
            }
        }

        @SuppressLint("ApplySharedPref", "UseKtx")
        set(value) {
            prefs.edit().putString(KEY_EXTERNAL_DEVICE_ID, value).commit()
        }

    companion object {
        private const val KEY_EXTERNAL_DEVICE_ID = "KEY_EXTERNAL_DEVICE_ID"
    }
}
