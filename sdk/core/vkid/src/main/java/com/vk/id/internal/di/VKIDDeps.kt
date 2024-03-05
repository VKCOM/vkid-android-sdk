package com.vk.id.internal.di

import com.vk.id.AuthOptionsCreator
import com.vk.id.AuthResultHandler
import com.vk.id.exchangetoken.VKIDTokenExchanger
import com.vk.id.internal.api.VKIDApi
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.ipc.SilentAuthInfoProvider
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.logout.VKIDLoggerOut
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.refreshuser.VKIDUserRefresher
import com.vk.id.storage.TokenStorage

internal interface VKIDDeps {
    val authProvidersChooser: Lazy<AuthProvidersChooser>
    val authOptionsCreator: AuthOptionsCreator
    val authCallbacksHolder: AuthCallbacksHolder
    val authResultHandler: Lazy<AuthResultHandler>
    val dispatchers: CoroutinesDispatchers
    val vkSilentAuthInfoProvider: Lazy<SilentAuthInfoProvider>
    val userDataFetcher: Lazy<UserDataFetcher>
    val api: Lazy<VKIDApi>
    val tokenRefresher: Lazy<VKIDTokenRefresher>
    val tokenExchanger: Lazy<VKIDTokenExchanger>
    val userRefresher: Lazy<VKIDUserRefresher>
    val loggerOut: Lazy<VKIDLoggerOut>
    val tokenStorage: TokenStorage
}
