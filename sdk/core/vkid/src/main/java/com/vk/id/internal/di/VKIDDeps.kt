@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.di

import com.vk.id.AuthOptionsCreator
import com.vk.id.AuthResultHandler
import com.vk.id.analytics.stat.StatTracker
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.ipc.SilentAuthInfoProvider
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.network.VKIDApi

internal interface VKIDDeps {
    val api: Lazy<VKIDApi>
    val authCallbacksHolder: AuthCallbacksHolder
    val authOptionsCreator: AuthOptionsCreator
    val authProvidersChooser: Lazy<AuthProvidersChooser>
    val authResultHandler: Lazy<AuthResultHandler>
    val dispatchers: CoroutinesDispatchers
    val statTracker: StatTracker
    val userDataFetcher: Lazy<UserDataFetcher>
    val vkSilentAuthInfoProvider: Lazy<SilentAuthInfoProvider>
}
