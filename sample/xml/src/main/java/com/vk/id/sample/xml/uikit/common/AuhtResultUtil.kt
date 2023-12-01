package com.vk.id.sample.xml.uikit.common

import android.content.Context
import android.widget.Toast
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail

private const val TOKEN_VISIBLE_CHARACTERS = 10

private var currentToast: Toast? = null

public fun showToast(context: Context, text: String) {
    currentToast?.cancel()
    currentToast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
    currentToast?.show()
}

public fun formatToken(token: String): String = token.hideLastCharacters(TOKEN_VISIBLE_CHARACTERS)

public fun String.hideLastCharacters(firstCharactersToKeepVisible: Int): String {
    return if (this.length <= firstCharactersToKeepVisible) {
        this
    } else {
        this.substring(0, firstCharactersToKeepVisible) + "..."
    }
}

public fun onVKIDAuthSuccess(context: Context, accessToken: AccessToken) {
    val token = formatToken(accessToken.token)
    showToast(context, "There is token: $token")
}

public fun onVKIDAuthFail(context: Context, fail: VKIDAuthFail) {
    when (fail) {
        is VKIDAuthFail.Canceled -> {
            showToast(context, "Auth canceled")
        }

        else -> {
            showToast(context, "Something wrong: ${fail.description}")
        }
    }
}
