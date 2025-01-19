@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.di

import com.vk.id.AuthOptionsCreator
import com.vk.id.AuthResultHandler
import com.vk.id.analytics.stat.StatTracker
import com.vk.id.common.InternalVKIDApi
import com.vk.id.exchangetoken.VKIDTokenExchanger
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.context.InternalVKIDActivityStarter
import com.vk.id.internal.context.InternalVKIDPackageManager
import com.vk.id.internal.ipc.SilentAuthInfoProvider
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.logout.VKIDLoggerOut
import com.vk.id.network.InternalVKIDApiContract
import com.vk.id.network.groupsubscription.InternalVKIDGroupSubscriptionApiService
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.refreshuser.VKIDUserRefresher
import com.vk.id.storage.InternalVKIDEncryptedSharedPreferencesStorage
import com.vk.id.storage.InternalVKIDTokenStorage

internal interface VKIDDeps {
    val serviceCredentials: Lazy<ServiceCredentials>
    val authCallbacksHolder: AuthCallbacksHolder
    val authOptionsCreator: AuthOptionsCreator
    val authProvidersChooser: Lazy<AuthProvidersChooser>
    val authResultHandler: Lazy<AuthResultHandler>
    val dispatchers: VKIDCoroutinesDispatchers
    val vkSilentAuthInfoProvider: Lazy<SilentAuthInfoProvider>
    val userDataFetcher: Lazy<UserDataFetcher>
    val api: Lazy<InternalVKIDApiContract>
    val tokenRefresher: Lazy<VKIDTokenRefresher>
    val tokenExchanger: Lazy<VKIDTokenExchanger>
    val userRefresher: Lazy<VKIDUserRefresher>
    val loggerOut: Lazy<VKIDLoggerOut>
    val tokenStorage: InternalVKIDTokenStorage
    val deviceIdStorage: Lazy<InternalVKIDDeviceIdProvider.DeviceIdStorage>
    val prefsStore: Lazy<InternalVKIDPrefsStore>
    val encryptedSharedPreferencesStorage: Lazy<InternalVKIDEncryptedSharedPreferencesStorage>
    val statTracker: StatTracker
    val vkidPackageManager: InternalVKIDPackageManager
    val activityStarter: InternalVKIDActivityStarter
    val isFlutter: Boolean
    val groupSubscriptionApiService: Lazy<InternalVKIDGroupSubscriptionApiService>
}
