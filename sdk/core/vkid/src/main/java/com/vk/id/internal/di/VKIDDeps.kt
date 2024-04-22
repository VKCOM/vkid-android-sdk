@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.di

import com.vk.id.AuthOptionsCreator
import com.vk.id.AuthResultHandler
import com.vk.id.common.InternalVKIDApi
import com.vk.id.exchangetoken.VKIDTokenExchanger
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.device.VKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.ipc.SilentAuthInfoProvider
import com.vk.id.internal.store.VKIDPrefsStore
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.logout.VKIDLoggerOut
import com.vk.id.network.VKIDApi
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.refreshuser.VKIDUserRefresher
import com.vk.id.storage.TokenStorage
import com.vk.id.storage.VKIDEncryptedSharedPreferencesStorage

internal interface VKIDDeps {
    val authProvidersChooser: Lazy<AuthProvidersChooser>
    val authOptionsCreator: AuthOptionsCreator
    val authCallbacksHolder: AuthCallbacksHolder
    val authResultHandler: Lazy<AuthResultHandler>
    val dispatchers: VKIDCoroutinesDispatchers
    val vkSilentAuthInfoProvider: Lazy<SilentAuthInfoProvider>
    val userDataFetcher: Lazy<UserDataFetcher>
    val api: Lazy<VKIDApi>
    val tokenRefresher: Lazy<VKIDTokenRefresher>
    val tokenExchanger: Lazy<VKIDTokenExchanger>
    val userRefresher: Lazy<VKIDUserRefresher>
    val loggerOut: Lazy<VKIDLoggerOut>
    val tokenStorage: TokenStorage
    val deviceIdStorage: Lazy<VKIDDeviceIdProvider.DeviceIdStorage>
    val prefsStore: Lazy<VKIDPrefsStore>
    val encryptedSharedPreferencesStorage: Lazy<VKIDEncryptedSharedPreferencesStorage>
}
