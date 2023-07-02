package com.vk.id.internal.di

import android.content.Context
import com.vk.id.internal.api.VKIDApi
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthProvidersChooserDefault
import com.vk.id.internal.auth.device.DeviceIdPrefs
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.store.PrefsStore
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.util.lazyUnsafe
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

internal class VKIDDepsProd(
    override val appContext: Context
) : VKIDDeps {

    override val api: Lazy<VKIDApiService> = lazyUnsafe {
        // certs? ssl pinning?
        val client = OkHttpClient.Builder()
            .readTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
        val api = VKIDApi(client)
        VKIDApiService(api)
    }

    override val authProvidersChooser: Lazy<AuthProvidersChooser> = lazyUnsafe {
        AuthProvidersChooserDefault(appContext)
    }

    override val prefsStore: Lazy<PrefsStore> = lazyUnsafe {
        PrefsStore(appContext)
    }

    override val deviceIdProvider: Lazy<DeviceIdProvider> = lazyUnsafe {
        DeviceIdProvider(DeviceIdPrefs(appContext))
    }

    override val pkceGenerator: Lazy<PkceGeneratorSHA256> = lazyUnsafe {
        PkceGeneratorSHA256()
    }

    private companion object {
        private const val OKHTTP_TIMEOUT_SECONDS = 60L
    }
}