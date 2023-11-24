package com.vk.id.sample.app.screen.styling.util

import android.content.Context
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.sample.xml.uikit.common.formatToken
import com.vk.id.sample.xml.uikit.common.showToast

internal fun onVKIDAuthSuccess(context: Context, accessToken: AccessToken) {
    val token = formatToken(accessToken.token)
    showToast(context, "There is token: $token")
}

internal fun onVKIDAuthFail(context: Context, fail: VKIDAuthFail) {
    when (fail) {
        is VKIDAuthFail.Canceled -> {
            showToast(context, "Auth canceled")
        }

        else -> {
            showToast(context, "Something wrong: ${fail.description}")
        }
    }
}
