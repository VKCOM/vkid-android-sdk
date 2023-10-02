package com.vk.id.internal.auth

import android.content.Context

internal fun interface VKIDAuthProvider {
    fun auth(context: Context, authOptions: AuthOptions)
}
