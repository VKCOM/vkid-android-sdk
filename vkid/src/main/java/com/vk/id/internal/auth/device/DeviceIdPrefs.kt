/**
 * Copyright (c) 2020 - present, LLC “V Kontakte”
 *
 * 1. Permission is hereby granted to any person obtaining a copy of this Software to
 * use the Software without charge.
 *
 * 2. Restrictions
 * You may not modify, merge, publish, distribute, sublicense, and/or sell copies,
 * create derivative works based upon the Software or any part thereof.
 *
 * 3. Termination
 * This License is effective until terminated. LLC “V Kontakte” may terminate this
 * License at any time without any negative consequences to our rights.
 * You may terminate this License at any time by deleting the Software and all copies
 * thereof. Upon termination of this license for any reason, you shall continue to be
 * bound by the provisions of Section 2 above.
 * Termination will be without prejudice to any rights LLC “V Kontakte” may have as
 * a result of this agreement.
 *
 * 4. Disclaimer of warranty and liability
 * THE SOFTWARE IS MADE AVAILABLE ON THE “AS IS” BASIS. LLC “V KONTAKTE” DISCLAIMS
 * ALL WARRANTIES THAT THE SOFTWARE MAY BE SUITABLE OR UNSUITABLE FOR ANY SPECIFIC
 * PURPOSES OF USE. LLC “V KONTAKTE” CAN NOT GUARANTEE AND DOES NOT PROMISE ANY
 * SPECIFIC RESULTS OF USE OF THE SOFTWARE.
 * UNDER NO CIRCUMSTANCES LLC “V KONTAKTE” BEAR LIABILITY TO THE LICENSEE OR ANY
 * THIRD PARTIES FOR ANY DAMAGE IN CONNECTION WITH USE OF THE SOFTWARE.
*/
package com.vk.id.internal.auth.device

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

internal class DeviceIdPrefs(private val context: Context) : DeviceIdProvider.DeviceIdStorage {
    override fun getDeviceId(): String = getPrefs(context).getString(DEVICE_ID, "") ?: ""

    override fun setDeviceId(deviceId: String) {
        getPrefs(context).edit().putString(DEVICE_ID, deviceId).apply()
    }

    override fun getSystemDeviceId(): String = getPrefs(context).getString(SYSTEM_DEVICE_ID, "") ?: ""

    override fun setSystemDeviceId(systemDeviceId: String) {
        getPrefs(context).edit().putString(SYSTEM_DEVICE_ID, systemDeviceId).apply()
    }

    override fun getDeviceToken(memberId: Long): String =
        getPrefs(context).getString(deviceTokenPreferenceKey(memberId), "") ?: ""

    override fun setDeviceToken(memberId: Long, deviceToken: String) {
        getPrefs(context).edit().putString(deviceTokenPreferenceKey(memberId), deviceToken).apply()
    }

    override fun clearDeviceToken(memberId: Long) {
        getPrefs(context).edit().remove(deviceTokenPreferenceKey(memberId)).apply()
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun deviceTokenPreferenceKey(memberId: Long): String =
        "$DEVICE_TOKEN_PREFIX$memberId"

    private companion object {
        private const val DEVICE_ID = "__vk_device_id__"
        private const val SYSTEM_DEVICE_ID = "__vk_system_device_id__"
        private const val DEVICE_TOKEN_PREFIX = "__vk__device_token__"
    }
}
