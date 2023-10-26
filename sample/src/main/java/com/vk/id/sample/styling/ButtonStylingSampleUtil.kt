package com.vk.id.sample.styling

import android.content.Context
import android.widget.Toast
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail

private const val TOKEN_VISIBLE_CHARACTERS = 10

internal fun onVKIDAuthSuccess(context: Context, accessToken: AccessToken) {
    val token = accessToken.token.hideLastCharacters(TOKEN_VISIBLE_CHARACTERS)
    showToast(context, "There is token: $token")
}

private fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
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

private fun String.hideLastCharacters(firstCharactersToKeepVisible: Int): String {
    return if (this.length <= firstCharactersToKeepVisible) {
        this
    } else {
        this.substring(0, firstCharactersToKeepVisible) + "..."
    }
}
