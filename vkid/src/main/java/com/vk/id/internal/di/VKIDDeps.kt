package com.vk.id.internal.di

import android.content.Context
import com.vk.id.AuthOptionsCreator
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.app.SilentAuthServicesProvider
import com.vk.id.internal.auth.app.TrustedProvidersCache
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.ipc.VkSilentAuthInfoProvider
import com.vk.id.internal.store.PrefsStore
import com.vk.id.internal.user.UserDataFetcher

internal interface VKIDDeps {
    val api: Lazy<VKIDApiService>
    val appContext: Context
    val authProvidersChooser: Lazy<AuthProvidersChooser>
    val authOptionsCreator: AuthOptionsCreator
    val deviceIdProvider: Lazy<DeviceIdProvider>
    val dispatchers: CoroutinesDispatchers
    val prefsStore: Lazy<PrefsStore>
    val pkceGenerator: Lazy<PkceGeneratorSHA256>
    val serviceCredentials: Lazy<ServiceCredentials>
    val silentAuthServicesProvider: Lazy<SilentAuthServicesProvider>
    val trustedProvidersCache: Lazy<TrustedProvidersCache>
    val vkSilentAuthInfoProvider: Lazy<VkSilentAuthInfoProvider>
    val userDataFetcher: Lazy<UserDataFetcher>
}
