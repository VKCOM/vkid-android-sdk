@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.di

import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.vk.id.AuthOptionsCreator
import com.vk.id.AuthResultHandler
import com.vk.id.TokensHandler
import com.vk.id.common.InternalVKIDApi
import com.vk.id.exchangetoken.VKIDTokenExchanger
import com.vk.id.fetchuser.VKIDUserInfoFetcher
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthActivity
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthProvidersChooserDefault
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.app.SilentAuthServicesProvider
import com.vk.id.internal.auth.app.TrustedProvidersCache
import com.vk.id.internal.auth.device.DeviceIdPrefs
import com.vk.id.internal.auth.device.VKIDDeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.concurrent.CoroutinesDispatchersProd
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.ipc.SilentAuthInfoProvider
import com.vk.id.internal.ipc.VkSilentAuthInfoProvider
import com.vk.id.internal.state.StateGenerator
import com.vk.id.internal.store.VKIDPrefsStore
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.logout.VKIDLoggerOut
import com.vk.id.network.VKIDApi
import com.vk.id.network.VKIDRealApi
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.refreshuser.VKIDUserRefresher
import com.vk.id.storage.TokenStorage
import com.vk.id.storage.VKIDEncryptedSharedPreferencesStorage

internal open class VKIDDepsProd(
    private val appContext: Context
) : VKIDDeps {

    private val serviceCredentials: Lazy<ServiceCredentials> = lazy {
        val componentName = ComponentName(appContext, AuthActivity::class.java)
        val flags = PackageManager.GET_META_DATA or PackageManager.GET_ACTIVITIES
        val ai: ActivityInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appContext.packageManager.getActivityInfo(
                componentName,
                PackageManager.ComponentInfoFlags.of(flags.toLong())
            )
        } else {
            appContext.packageManager.getActivityInfo(
                componentName,
                flags
            )
        }
        val clientID = ai.metaData.getIntOrThrow("VKIDClientID").toString()
        val clientSecret = ai.metaData.getStringOrThrow("VKIDClientSecret")
        val redirectScheme = ai.metaData.getStringOrThrow("VKIDRedirectScheme")
        val redirectHost = ai.metaData.getStringOrThrow("VKIDRedirectHost")
        val redirectUri = "$redirectScheme://$redirectHost"

        ServiceCredentials(clientID, clientSecret, redirectUri)
    }

    private val silentAuthServicesProvider: Lazy<SilentAuthServicesProvider> = lazy {
        SilentAuthServicesProvider(
            appContext,
            trustedProvidersCache.value
        )
    }

    override val api: Lazy<VKIDApi> = lazy {
        VKIDRealApi.getInstance(context = appContext)
    }
    private val apiService = lazy { VKIDApiService(api.value) }

    private val trustedProvidersCache = lazy {
        val creds = serviceCredentials.value
        TrustedProvidersCache(apiService, creds.clientID, creds.clientSecret, dispatchers)
    }

    override val vkSilentAuthInfoProvider: Lazy<SilentAuthInfoProvider> = lazy {
        VkSilentAuthInfoProvider(
            context = appContext,
            servicesProvider = silentAuthServicesProvider.value,
            deviceIdProvider = deviceIdProvider.value,
        )
    }

    override val userDataFetcher: Lazy<UserDataFetcher> = lazy {
        UserDataFetcher(
            dispatchers = dispatchers,
            serviceCredentials = serviceCredentials.value,
            vkSilentAuthInfoProvider = vkSilentAuthInfoProvider.value,
        )
    }

    override val authProvidersChooser: Lazy<AuthProvidersChooser> = lazy {
        AuthProvidersChooserDefault(
            appContext,
            SilentAuthServicesProvider(
                appContext,
                trustedProvidersCache.value
            )
        )
    }

    override val authOptionsCreator: AuthOptionsCreator by lazy {
        AuthOptionsCreator(
            appContext = appContext,
            pkceGenerator = pkceGenerator,
            prefsStore = prefsStore,
            serviceCredentials = serviceCredentials,
            stateGenerator = stateGenerator
        )
    }

    override val authCallbacksHolder = AuthCallbacksHolder()

    override val authResultHandler: Lazy<AuthResultHandler> = lazy {
        AuthResultHandler(
            dispatchers = dispatchers,
            callbacksHolder = authCallbacksHolder,
            deviceIdProvider = deviceIdProvider.value,
            prefsStore = prefsStore.value,
            serviceCredentials = serviceCredentials.value,
            api = apiService.value,
            tokensHandler = tokensHandler.value,
            loggerOut = loggerOut.value,
            tokenStorage = tokenStorage,
        )
    }
    override val tokenRefresher: Lazy<VKIDTokenRefresher> = lazy {
        VKIDTokenRefresher(
            api = apiService.value,
            tokenStorage = tokenStorage,
            deviceIdProvider = deviceIdProvider.value,
            serviceCredentials = serviceCredentials.value,
            stateGenerator = stateGenerator,
            tokensHandler = tokensHandler.value,
            dispatchers = dispatchers,
            prefsStore = prefsStore.value,
        )
    }
    override val tokenExchanger: Lazy<VKIDTokenExchanger> = lazy {
        VKIDTokenExchanger(
            api = apiService.value,
            deviceIdProvider = deviceIdProvider.value,
            serviceCredentials = serviceCredentials.value,
            stateGenerator = stateGenerator,
            tokensHandler = tokensHandler.value,
            dispatchers = dispatchers,
            prefsStore = prefsStore.value,
            pkceGenerator = pkceGenerator.value,
        )
    }
    override val userRefresher: Lazy<VKIDUserRefresher> = lazy {
        VKIDUserRefresher(
            api = apiService.value,
            tokenStorage = tokenStorage,
            deviceIdProvider = deviceIdProvider.value,
            serviceCredentials = serviceCredentials.value,
            dispatchers = dispatchers,
            refresher = tokenRefresher.value,
        )
    }
    override val loggerOut: Lazy<VKIDLoggerOut> = lazy {
        VKIDLoggerOut(
            api = apiService.value,
            tokenStorage = tokenStorage,
            deviceIdProvider = deviceIdProvider.value,
            serviceCredentials = serviceCredentials.value,
            dispatchers = dispatchers,
        )
    }

    override val encryptedSharedPreferencesStorage: Lazy<VKIDEncryptedSharedPreferencesStorage> = lazy {
        VKIDEncryptedSharedPreferencesStorage(appContext)
    }

    override val tokenStorage by lazy { TokenStorage(encryptedSharedPreferencesStorage.value) }

    private val userInfoFetcher: Lazy<VKIDUserInfoFetcher> = lazy {
        VKIDUserInfoFetcher(
            api = apiService.value,
            serviceCredentials = serviceCredentials.value,
            dispatchers = dispatchers,
            deviceIdProvider = deviceIdProvider.value,
        )
    }

    private val tokensHandler = lazy {
        TokensHandler(
            userInfoFetcher.value,
            tokenStorage,
            dispatchers,
        )
    }

    private val stateGenerator by lazy { StateGenerator(prefsStore.value) }

    override val prefsStore: Lazy<VKIDPrefsStore> = lazy {
        VKIDPrefsStore(appContext)
    }

    override val deviceIdStorage: Lazy<VKIDDeviceIdProvider.DeviceIdStorage> = lazy {
        DeviceIdPrefs(appContext)
    }

    private val deviceIdProvider: Lazy<VKIDDeviceIdProvider> = lazy {
        VKIDDeviceIdProvider(appContext, deviceIdStorage.value)
    }

    private val pkceGenerator: Lazy<PkceGeneratorSHA256> = lazy {
        PkceGeneratorSHA256()
    }

    override val dispatchers: VKIDCoroutinesDispatchers
        get() = CoroutinesDispatchersProd()
}

private const val MISSED_PLACEHOLDER_ERROR_MESSAGE =
    "VKID initialization error. Missing %s parameter in manifest placeholders"

private fun Bundle.getIntOrThrow(key: String): Int {
    val value = getInt(key, -1)
    if (value == -1) {
        throw IllegalStateException(MISSED_PLACEHOLDER_ERROR_MESSAGE.format(key))
    }
    return value
}

private fun Bundle.getStringOrThrow(key: String): String {
    return getString(key)
        ?: throw IllegalStateException(MISSED_PLACEHOLDER_ERROR_MESSAGE.format(key))
}
