package com.vk.id.internal.di

import android.content.Context
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthProvidersChooserDefault
import com.vk.id.internal.util.lazyUnsafe

internal class VKIDDepsProd(
    override val appContext: Context
) : VKIDDeps {

    override val authProvidersChooser: Lazy<AuthProvidersChooser> = lazyUnsafe {
        AuthProvidersChooserDefault(appContext)
    }
}