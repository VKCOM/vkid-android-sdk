package com.vk.id.internal.auth

internal interface AuthProvidersChooser {
    fun chooseBest(): VKIDAuthProvider
}