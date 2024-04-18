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
@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.auth.device

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.util.MD5
import com.vk.id.logger.createLoggerForClass
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@InternalVKIDApi
public class DeviceIdProvider internal constructor(
    private val context: Context,
    private val deviceIdStorage: DeviceIdStorage
) {
    private val mutex = ReentrantLock()
    private val logger = createLoggerForClass()

    @InternalVKIDApi
    public interface DeviceIdStorage {
        public fun getDeviceId(): String
        public fun setDeviceId(deviceId: String)

        public fun getSystemDeviceId(): String
        public fun setSystemDeviceId(systemDeviceId: String)

        public fun getDeviceToken(memberId: Long): String
        public fun setDeviceToken(memberId: Long, deviceToken: String)
        public fun clearDeviceToken(memberId: Long)
    }

    internal fun setDeviceId(deviceId: String) {
        deviceIdStorage.setDeviceId(deviceId)
    }

    internal fun getDeviceId(): String {
        mutex.withLock {
            val deviceId = deviceIdStorage.getDeviceId()
            return if (TextUtils.isEmpty(deviceId)) {
                logger.debug("nextDeviceId is null or empty: $deviceId")
                val androidId = findDeviceIdByAndroidId(context)

                val ids = ArrayList<String?>()
                ids.add(if (!TextUtils.isEmpty(androidId)) androidId else DEFAULT_ID)

                val sb = StringBuilder()
                for (i in ids.indices) {
                    sb.append(ids[i])
                    if (i < ids.size - 1) {
                        sb.append(":")
                    }
                }
                val nextDeviceId = sb.toString()
                logger.debug("new nextDeviceId: $nextDeviceId")
                deviceIdStorage.setDeviceId(nextDeviceId)
                nextDeviceId
            } else {
                deviceId
            }
        }
    }

    internal companion object {
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
