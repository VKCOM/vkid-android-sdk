package com.vk.id.internal.di

import android.content.Context
import com.vk.id.internal.auth.AuthProvidersChooser

internal interface VKIDDeps {
    val appContext: Context
    val authProvidersChooser: Lazy<AuthProvidersChooser>
}