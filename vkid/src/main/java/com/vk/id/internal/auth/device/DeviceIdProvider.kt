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

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.vk.id.internal.log.createLoggerForClass
import com.vk.id.internal.util.MD5

internal class DeviceIdProvider(private val deviceIdStorage: DeviceIdStorage) {
    private val logger = createLoggerForClass()
    private var nextDeviceId = String()

    interface DeviceIdStorage {
        fun getDeviceId(): String
        fun setDeviceId(deviceId: String)

        fun getSystemDeviceId(): String
        fun setSystemDeviceId(systemDeviceId: String)

        fun getDeviceToken(memberId: Long): String
        fun setDeviceToken(memberId: Long, deviceToken: String)
        fun clearDeviceToken(memberId: Long)
    }

    fun getDeviceId(context: Context): String {
        if (nextDeviceId.isNotEmpty()) {
            return nextDeviceId
        }
        logger.debug("nextDeviceId is null or empty: $nextDeviceId")
        nextDeviceId = deviceIdStorage.getDeviceId()
        if (TextUtils.isEmpty(nextDeviceId)) {
            val androidId = findDeviceIdByAndroidId(context)
            val deviceId = getDeviceId()

            val ids = ArrayList<String?>()
            ids.add(if (!TextUtils.isEmpty(androidId)) androidId else DEFAULT_ID)
            ids.add(if (!TextUtils.isEmpty(deviceId)) deviceId else DEFAULT_ID)

            val sb = StringBuilder()
            for (i in ids.indices) {
                sb.append(ids[i])
                if (i < ids.size - 1) {
                    sb.append(":")
                }
            }
            nextDeviceId = sb.toString()
            deviceIdStorage.setDeviceId(nextDeviceId)
        }
        logger.debug("new nextDeviceId: $nextDeviceId")
        return nextDeviceId
    }

    companion object {
        @SuppressLint("HardwareIds")
        private fun findDeviceIdByAndroidId(context: Context): String? {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun getDeviceId(): String {
            val sb = StringBuilder()
                .append(Build.PRODUCT)
                .append(Build.BOARD)
                .append(Build.BOOTLOADER)
                .append(Build.BRAND)
                .append(Build.DEVICE)
                .append(Build.DISPLAY)
                .append(Build.FINGERPRINT)
                .append(Build.HARDWARE)
                .append(Build.HOST)
                .append(Build.ID)
                .append(Build.MANUFACTURER)
                .append(Build.MODEL)
                .append(Build.PRODUCT)
                .append(Build.TAGS)
                .toString()
            return MD5.convert(sb)
        }

        private const val DEFAULT_ID = "default"
    }
}
