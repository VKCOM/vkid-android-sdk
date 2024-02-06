package com.vk.id.storage

import com.vk.id.AccessToken

internal class TokenStorage(
    private val preferences: EncryptedSharedPreferencesStorage
) {
    var accessToken: AccessToken? = null
    var refreshToken: String?
        get() = preferences.getString(REFRESH_TOKEN_KEY)
        set(value) = preferences.set(REFRESH_TOKEN_KEY, value)

    companion object {
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
    }
}
