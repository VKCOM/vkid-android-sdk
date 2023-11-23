package com.vk.id.internal.di

import com.vk.AuthCallbacksHolder
import com.vk.id.AuthOptionsCreator
import com.vk.id.AuthResultHandler
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
    val authProvidersChooser: Lazy<AuthProvidersChooser>
    val authOptionsCreator: AuthOptionsCreator
    val authCallbacksHolder: AuthCallbacksHolder
    val authResultHandler: Lazy<AuthResultHandler>
    val dispatchers: CoroutinesDispatchers
    val vkSilentAuthInfoProvider: Lazy<VkSilentAuthInfoProvider>
    val userDataFetcher: Lazy<UserDataFetcher>
}
