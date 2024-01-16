package com.vk.id.internal.di

import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.vk.id.AuthOptionsCreator
import com.vk.id.AuthResultHandler
import com.vk.id.VKID
import com.vk.id.internal.api.VKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.api.VKIDRealApi
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
import com.vk.id.internal.store.PrefsStore
import com.vk.id.internal.user.UserDataFetcher
import okhttp3.CertificatePinner
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

    override val api: Lazy<VKIDApi> = lazy {
        val client = OkHttpClient.Builder()
            .readTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor())
            .addCertificatePinnerIfNecessary()
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
            api = apiService.value
        )
    }

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

    private fun OkHttpClient.Builder.addCertificatePinnerIfNecessary(): OkHttpClient.Builder {
        if (!isDebuggable()) {
            certificatePinner(
                CertificatePinner.Builder()
                    .add(HOST_NAME_API, HOST_CERTIFICATE_HASH_1)
                    .add(HOST_NAME_API, HOST_CERTIFICATE_HASH_2)
                    .add(HOST_NAME_API, HOST_CERTIFICATE_HASH_3)
                    .add(HOST_NAME_OAUTH, HOST_CERTIFICATE_HASH_1)
                    .add(HOST_NAME_OAUTH, HOST_CERTIFICATE_HASH_2)
                    .add(HOST_NAME_OAUTH, HOST_CERTIFICATE_HASH_3)
                    .build()
            )
        }
        return this
    }

    private fun isDebuggable() = appContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    private companion object {
        private const val HOST_NAME_API = "api.vk.com"
        private const val HOST_NAME_OAUTH = "oauth.vk.com"
        private const val HOST_CERTIFICATE_HASH_1 = "sha256/p+lqTZ1LH3x8myQuyq7TpS5Acm5DkluDFCFB1Xnqc/4="
        private const val HOST_CERTIFICATE_HASH_2 = "sha256/IQBnNBEiFuhj+8x6X8XLgh01V9Ic5/V3IRQLNFFc7v4="
        private const val HOST_CERTIFICATE_HASH_3 = "sha256/K87oWBWM9UZfyddvDfoxL+8lpNyoUB2ptGtn0fv6G2Q="
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
