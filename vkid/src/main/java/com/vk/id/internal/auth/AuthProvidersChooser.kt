package com.vk.id.internal.auth

internal interface AuthProvidersChooser {
    suspend fun chooseBest(): VKIDAuthProvider
}