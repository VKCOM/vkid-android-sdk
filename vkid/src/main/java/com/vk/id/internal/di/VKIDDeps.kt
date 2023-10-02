package com.vk.id.internal.di

import android.content.Context
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.app.TrustedProvidersCache
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.store.PrefsStore

internal interface VKIDDeps {
    val api: Lazy<VKIDApiService>
    val appContext: Context
    val authProvidersChooser: Lazy<AuthProvidersChooser>
    val deviceIdProvider: Lazy<DeviceIdProvider>
    val dispatchers: CoroutinesDispatchers
    val prefsStore: Lazy<PrefsStore>
    val pkceGenerator: Lazy<PkceGeneratorSHA256>
    val serviceCredentials: Lazy<ServiceCredentials>
    val trustedProvidersCache: Lazy<TrustedProvidersCache>
}
