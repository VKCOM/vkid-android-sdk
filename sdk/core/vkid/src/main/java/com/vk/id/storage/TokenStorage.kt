package com.vk.id.storage

import com.vk.id.AccessToken

internal class TokenStorage(
    private val preferences: EncryptedSharedPreferencesStorage
) {
    var accessToken: AccessToken? = null
    var idToken: String?
        get() = preferences.getString(ID_TOKEN_KEY)
        set(value) = preferences.set(ID_TOKEN_KEY, value)
    var refreshToken: String?
        get() = preferences.getString(REFRESH_TOKEN_KEY)
        set(value) = preferences.set(REFRESH_TOKEN_KEY, value)

    companion object {
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
        private const val ID_TOKEN_KEY = "ID_TOKEN_KEY"
    }
}
