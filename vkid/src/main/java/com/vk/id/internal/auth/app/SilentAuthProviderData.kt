package com.vk.id.internal.auth.app

import android.content.ComponentName

internal data class SilentAuthProviderData(
    val componentName: ComponentName,
    val weight: Int,
)
