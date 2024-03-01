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
import com.vk.id.VKID
import com.vk.id.exchangetoken.VKIDTokenExchanger
import com.vk.id.fetchuser.VKIDUserInfoFetcher
import com.vk.id.internal.api.VKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.api.VKIDRealApi
import com.vk.id.internal.api.sslpinning.SslPinningProvider
import com.vk.id.internal.api.useragent.UserAgentInterceptor
import com.vk.id.internal.api.useragent.UserAgentProvider
import com.vk.id.internal.auth.AuthActivity
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthProvidersChooserDefault
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.app.SilentAuthServicesProvider
import com.vk.id.internal.auth.app.TrustedProvidersCache
import com.vk.id.internal.auth.device.DeviceIdPrefs
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.concurrent.CoroutinesDispatchersProd
import com.vk.id.internal.ipc.SilentAuthInfoProvider
import com.vk.id.internal.ipc.VkSilentAuthInfoProvider
import com.vk.id.internal.log.createLoggerForClass
import com.vk.id.internal.state.StateGenerator
import com.vk.id.internal.store.PrefsStore
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.storage.EncryptedSharedPreferencesStorage
import com.vk.id.storage.TokenStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

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

    private val sslPinningProvider = SslPinningProvider(appContext)

    override val api: Lazy<VKIDApi> = lazy {
        val client = OkHttpClient.Builder()
            .readTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor())
            .addInterceptor(UserAgentInterceptor(UserAgentProvider(appContext)))
            .let(sslPinningProvider::addSslPinning)
            .build()
        VKIDRealApi(client)
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
            deviceIdProvider = deviceIdProvider,
            stateGenerator = stateGenerator
        )
    }

    override val authCallbacksHolder = AuthCallbacksHolder()

    override val authResultHandler: Lazy<AuthResultHandler> = lazy {
        AuthResultHandler(
            appContext = appContext,
            dispatchers = dispatchers,
            callbacksHolder = authCallbacksHolder,
            deviceIdProvider = deviceIdProvider.value,
            prefsStore = prefsStore.value,
            serviceCredentials = serviceCredentials.value,
            api = apiService.value,
            tokensHandler = tokensHandler.value,
        )
    }
    override val tokenRefresher: Lazy<VKIDTokenRefresher> = lazy {
        VKIDTokenRefresher(
            context = appContext,
            api = apiService.value,
            tokenStorage = tokenStorage,
            deviceIdProvider = deviceIdProvider.value,
            serviceCredentials = serviceCredentials.value,
            stateGenerator = stateGenerator,
            tokensHandler = tokensHandler.value,
        )
    }
    override val tokenExchanger: Lazy<VKIDTokenExchanger> = lazy {
        VKIDTokenExchanger(
            context = appContext,
            api = apiService.value,
            deviceIdProvider = deviceIdProvider.value,
            serviceCredentials = serviceCredentials.value,
            stateGenerator = stateGenerator,
            tokensHandler = tokensHandler.value,
        )
    }

    private val userInfoFetcher: Lazy<VKIDUserInfoFetcher> = lazy {
        VKIDUserInfoFetcher(
            api = apiService.value,
            stateGenerator = stateGenerator,
            serviceCredentials = serviceCredentials.value,
        )
    }

    private val tokensHandler = lazy {
        TokensHandler(
            userInfoFetcher.value,
            tokenStorage
        )
    }

    private val stateGenerator by lazy { StateGenerator(prefsStore.value) }

    private val tokenStorage by lazy { TokenStorage(EncryptedSharedPreferencesStorage(appContext)) }

    private val prefsStore: Lazy<PrefsStore> = lazy {
        PrefsStore(appContext)
    }

    private val deviceIdProvider: Lazy<DeviceIdProvider> = lazy {
        DeviceIdProvider(DeviceIdPrefs(appContext))
    }

    private val pkceGenerator: Lazy<PkceGeneratorSHA256> = lazy {
        PkceGeneratorSHA256()
    }

    override val dispatchers: CoroutinesDispatchers
        get() = CoroutinesDispatchersProd()

    private fun loggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            private val logger = createLoggerForClass()
            override fun log(message: String) {
                if (VKID.logsEnabled) {
                    logger.debug(message)
                }
            }
        })
        logging.level = HttpLoggingInterceptor.Level.BASIC
        return logging
    }

    private companion object {
        private const val OKHTTP_TIMEOUT_SECONDS = 60L
    }
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
