package com.vk.id.sample.xml.uikit.common

import android.content.Context
import android.widget.Toast

private const val TOKEN_VISIBLE_CHARACTERS = 10

public fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

public fun formatToken(token: String): String = token.hideLastCharacters(TOKEN_VISIBLE_CHARACTERS)

public fun String.hideLastCharacters(firstCharactersToKeepVisible: Int): String {
    return if (this.length <= firstCharactersToKeepVisible) {
        this
    } else {
        this.substring(0, firstCharactersToKeepVisible) + "..."
    }
}
