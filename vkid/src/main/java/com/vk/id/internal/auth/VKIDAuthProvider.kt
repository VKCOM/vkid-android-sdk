package com.vk.id.internal.auth

internal fun interface VKIDAuthProvider {
    fun auth(authOptions: AuthOptions)
}
