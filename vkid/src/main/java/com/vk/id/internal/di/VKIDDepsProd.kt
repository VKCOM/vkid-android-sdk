package com.vk.id.internal.di

import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.vk.id.VKID
import com.vk.id.internal.api.VKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthActivity
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
import com.vk.id.internal.log.createLoggerForClass
import com.vk.id.internal.store.PrefsStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

internal class VKIDDepsProd(
    override val appContext: Context
) : VKIDDeps {

    override val serviceCredentials: Lazy<ServiceCredentials> = lazy {
        val componentName = ComponentName(appContext, AuthActivity::class.java)
        val flags = PackageManager.GET_META_DATA or PackageManager.GET_ACTIVITIES
        val ai: ActivityInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appContext.packageManager.getActivityInfo(
                componentName,
                PackageManager.ComponentInfoFlags.of(flags.toLong())
            )
        } else {
            @Suppress("DEPRECATION")
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

    override val api: Lazy<VKIDApiService> = lazy {
        val client = OkHttpClient.Builder()
            .readTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor())
            .build()
        val api = VKIDApi(client)
        VKIDApiService(api)
    }

    override val trustedProvidersCache = lazy {
        val creds = serviceCredentials.value
        TrustedProvidersCache(api, creds.clientID, creds.clientSecret, dispatchers)
    }

    override val authProvidersChooser: Lazy<AuthProvidersChooser> = lazy {
        AuthProvidersChooserDefault(SilentAuthServicesProvider(appContext, trustedProvidersCache.value))
    }

    override val prefsStore: Lazy<PrefsStore> = lazy {
        PrefsStore(appContext)
    }

    override val deviceIdProvider: Lazy<DeviceIdProvider> = lazy {
        DeviceIdProvider(DeviceIdPrefs(appContext))
    }

    override val pkceGenerator: Lazy<PkceGeneratorSHA256> = lazy {
        PkceGeneratorSHA256()
    }

    override val dispatchers: CoroutinesDispatchers
        get() = CoroutinesDispatchersProd()

    private fun loggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger {
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

private const val MISSED_PLACEHOLDER_ERROR_MESSAGE = "VKID initialization error. Missing %s parameter in manifest placeholders"

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
