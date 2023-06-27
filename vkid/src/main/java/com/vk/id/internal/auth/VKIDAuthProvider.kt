package com.vk.id.internal.auth

import android.app.Activity

internal fun interface VKIDAuthProvider {
    fun auth(activity: Activity, authOptions: AuthOptions)
}