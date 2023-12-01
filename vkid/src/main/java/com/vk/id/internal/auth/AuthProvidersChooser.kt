package com.vk.id.internal.auth

import com.vk.id.auth.VKIDAuthParams

internal interface AuthProvidersChooser {
    suspend fun chooseBest(params: VKIDAuthParams): VKIDAuthProvider
}
