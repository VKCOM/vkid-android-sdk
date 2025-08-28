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

package com.vk.id.internal.auth.app

import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Base64
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.context.InternalVKIDPackageManager
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Locale

internal object SilentAuthInfoUtils {

    fun calculateDigestHex(packageManager: InternalVKIDPackageManager, pkg: String): String? {
        return calculateDigest(packageManager, pkg, SilentAuthInfoUtils::calculateDigestHex)
    }

    fun calculateDigestBase64(signature: Signature): String {
        return calculateDigest(signature) { bytes ->
            Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }

    private fun calculateDigestHex(signature: Signature): String {
        return calculateDigest(signature) { bytes ->
            val bi = BigInteger(1, bytes)
            String.format("%0" + (bytes.size shl 1) + "X", bi).lowercase(Locale.ENGLISH)
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun calculateDigest(
        packageManager: InternalVKIDPackageManager,
        pkg: String,
        transform: (Signature) -> String
    ): String? {
        return try {
            packageManager
                .getPackageInfo(pkg, PackageManager.GET_SIGNATURES)
                .signatures
                ?.firstOrNull()
                ?.let {
                    transform(it)
                }
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateDigest(signature: Signature, transform: (ByteArray) -> String): String {
        val md = MessageDigest.getInstance("SHA")
        md.update(signature.toByteArray())
        return transform(md.digest())
    }
}
