/*
 * Copyright 2016 The AppAuth for Android Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.auth.web

import android.content.pm.PackageInfo
import android.content.pm.Signature
import android.util.Base64
import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.createLoggerForClass
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Represents a browser that may be used for an authorization flow.
 */
@Suppress("RedundantConstructorKeyword")
internal class BrowserDescriptor
/**
 * Creates a description of a browser from the core properties that are frequently used to
 * decide whether a browser can be used for an authorization flow. In most cases, it is
 * more convenient to use the other variant of the constructor that consumes a
 * [PackageInfo] object provided by the package manager.
 *
 * @param packageName     The Android package name of the browser.
 * @param signatureHashes The set of SHA-512, Base64 url safe encoded signatures for the app. This can be
 * generated for a signature by calling [.generateSignatureHash].
 * @param version         The version name of the browser.
 * @param useCustomTab    Whether it is intended to use the browser as a custom tab.
 */
constructor(
    /**
     * The package name of the browser app.
     */
    val packageName: String,
    /**
     * The set of [signatures][Signature] of the browser app,
     * which have been hashed with SHA-512, and Base-64 URL-safe encoded.
     */
    val signatureHashes: Set<String>,
    /**
     * The version string of the browser app.
     */
    val version: String,
    /**
     * Whether it is intended that the browser will be used via a custom tab.
     */
    val useCustomTab: Boolean
) {
    /**
     * Creates a description of a browser from a [PackageInfo] object returned from the
     * [android.content.pm.PackageManager]. The object is expected to include the
     * signatures of the app, which can be retrieved with the
     * [GET_SIGNATURES][android.content.pm.PackageManager.GET_SIGNATURES] flag when
     * calling [android.content.pm.PackageManager.getPackageInfo].
     */
    constructor(packageInfo: PackageInfo, useCustomTab: Boolean) : this(
        packageInfo.packageName,
        generateSignatureHashes(packageInfo.signatures),
        packageInfo.versionName,
        useCustomTab
    )

    /**
     * Creates a copy of this browser descriptor, changing the intention to use it as a custom
     * tab to the specified value.
     */
    fun changeUseCustomTab(newUseCustomTabValue: Boolean): BrowserDescriptor {
        return BrowserDescriptor(
            packageName,
            signatureHashes,
            version,
            newUseCustomTabValue
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || other !is BrowserDescriptor) {
            return false
        }
        return packageName == other.packageName &&
            version == other.version &&
            useCustomTab == other.useCustomTab &&
            signatureHashes == other.signatureHashes
    }

    override fun hashCode(): Int {
        var hash = packageName.hashCode()
        hash = PRIME_HASH_FACTOR * hash + version.hashCode()
        hash = PRIME_HASH_FACTOR * hash + if (useCustomTab) 1 else 0
        for (signatureHash in signatureHashes) {
            hash = PRIME_HASH_FACTOR * hash + signatureHash.hashCode()
        }
        return hash
    }

    companion object {
        // See: http://stackoverflow.com/a/2816747
        private const val PRIME_HASH_FACTOR = 92821
        private const val DIGEST_SHA_512 = "SHA-512"

        private val logger = createLoggerForClass()

        /**
         * Generates a SHA-512 hash, Base64 url-safe encoded, from a [Signature].
         */
        @Suppress("SwallowedException")
        fun generateSignatureHash(signature: Signature): String {
            return try {
                val digest = MessageDigest.getInstance(DIGEST_SHA_512)
                val hashBytes = digest.digest(signature.toByteArray())
                Base64.encodeToString(hashBytes, Base64.URL_SAFE or Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                logger.error("Can't generate signature hash", e)
                error("Platform does not support$DIGEST_SHA_512 hashing")
            }
        }

        /**
         * Generates a set of SHA-512, Base64 url-safe encoded signature hashes from the provided
         * array of signatures.
         */
        fun generateSignatureHashes(signatures: Array<Signature>): Set<String> {
            val signatureHashes: MutableSet<String> = HashSet()
            for (signature in signatures) {
                signatureHashes.add(generateSignatureHash(signature))
            }
            return signatureHashes
        }
    }
}
