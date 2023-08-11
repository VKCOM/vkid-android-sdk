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
package com.vk.id.internal.auth.app

import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.concurrent.LifecycleAwareExecutor
import java.util.concurrent.Future

/**
 * Class for cache providers for silent authorisation
 */
internal class TrustedProvidersCache(
    private val api: Lazy<VKIDApiService>,
    private val clientId: String,
    private val clientSecret: String,
    private val executor: LifecycleAwareExecutor
) {

    private var cachedTrustedProviders: List<VkAuthSilentAuthProvider>? = null
    private var prefetchFuture: Future<*>? = null

    // todo use in vkid.warmUp method
    fun prefetchSilentAuthProviders() {
        prefetchFuture = executor.submit { fetchSilentAuthProvidersSync() }
    }

    fun getSilentAuthProviders(): List<VkAuthSilentAuthProvider> {
        try {
            prefetchFuture?.get()
        } catch (_: Throwable) {
        }
        return cachedTrustedProviders ?: fetchSilentAuthProvidersSync()
    }

    private fun fetchSilentAuthProvidersSync(): List<VkAuthSilentAuthProvider> {
        return executor.executeCall(
            api.value.getSilentAuthProviders(clientId = clientId, clientSecret = clientSecret)
        )
            .getOrNull()
            .orEmpty()
            .plus(DEFAULT_TRUSTED_PROVIDERS)
            .also { cachedTrustedProviders = it }
    }

    companion object {
        private const val RELEASE_APP_SHA = "48761eef50ee53afc4cc9c5f10e6bde7f8f5b82f"
        private const val DEBUG_APP_SHA = "86259288a43f6c409a922bc3ce40ba08085bbadb"

        private val DEFAULT_TRUSTED_PROVIDERS = listOf(
            createBaseProviders(appPackage = "com.vkontakte.android", weight = 3),
            createBaseProviders(appPackage = "com.vk.im", weight = 2),
            createBaseProviders(appPackage = "com.vk.calls", weight = 1)
        ).flatten()

        private fun createBaseProviders(appPackage: String, weight: Int): List<VkAuthSilentAuthProvider> {
            return listOf(
                VkAuthSilentAuthProvider(appPackage, RELEASE_APP_SHA, weight),
                VkAuthSilentAuthProvider(appPackage, DEBUG_APP_SHA, weight),
            )
        }
    }
}
