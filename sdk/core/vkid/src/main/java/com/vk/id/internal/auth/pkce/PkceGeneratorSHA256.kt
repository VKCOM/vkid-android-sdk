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
package com.vk.id.internal.auth.pkce

import android.util.Base64
import com.vk.id.internal.log.createLoggerForClass
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

internal class PkceGeneratorSHA256 {

    private val logger = createLoggerForClass()

    fun generateRandomCodeVerifier(entropySource: SecureRandom): String {
        val randomBytes = ByteArray(MIN_CODE_VERIFIER_ENTROPY)
        entropySource.nextBytes(randomBytes)
        return Base64.encodeToString(randomBytes, PKCE_BASE64_ENCODE_SETTINGS)
    }

    fun deriveCodeVerifierChallenge(codeVerifier: String): String {
        return try {
            val sha256Digester = MessageDigest.getInstance(ALGORITHM)
            val input = codeVerifier.toByteArray(Charset.forName(CHARSET_NAME))
            sha256Digester.update(input)
            val digestBytes = sha256Digester.digest()
            Base64.encodeToString(digestBytes, PKCE_BASE64_ENCODE_SETTINGS)
        } catch (nsae: NoSuchAlgorithmException) {
            logger.error("SHA-256 algorithm not available", nsae)
            codeVerifier
        } catch (uee: UnsupportedEncodingException) {
            logger.error("Can't encode codeVerifier", uee)
            throw IllegalStateException("$CHARSET_NAME encoding not supported", uee)
        }
    }

    companion object {
        private const val ALGORITHM = "SHA-256"
        private const val CHARSET_NAME = "ISO_8859_1"
        private const val MIN_CODE_VERIFIER_ENTROPY = 128
        private const val PKCE_BASE64_ENCODE_SETTINGS =
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
    }
}
