package com.vk.id.internal.di

import android.content.Context
import com.vk.id.VKID
import com.vk.id.internal.api.VKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthProvidersChooserDefault
import com.vk.id.internal.auth.device.DeviceIdPrefs
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.external.SilentAuthServicesProvider
import com.vk.id.internal.auth.external.TrustedProvidersCache
import com.vk.id.internal.store.PrefsStore
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.concurrent.LifecycleAwareExecutor
import com.vk.id.internal.log.createLoggerForClass
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal class VKIDDepsProd(
    override val appContext: Context,
    clientId: String,
    clientSecret: String
) : VKIDDeps {

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
        TrustedProvidersCache(api, clientId, clientSecret, lifeCycleAwareExecutor.value)
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

    override val lifeCycleAwareExecutor: Lazy<LifecycleAwareExecutor> = lazy {
        LifecycleAwareExecutor(Executors.newCachedThreadPool())
    }

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