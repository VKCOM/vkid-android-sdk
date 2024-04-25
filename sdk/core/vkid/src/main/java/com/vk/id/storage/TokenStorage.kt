@file:OptIn(InternalVKIDApi::class)

package com.vk.id.storage

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.vk.id.AccessToken
import com.vk.id.common.InternalVKIDApi

internal class TokenStorage(
    private val preferences: EncryptedSharedPreferencesStorage
) {
    private val gson = Gson()
    var accessToken: AccessToken?
        get() = try {
            gson.fromJson(preferences.getString(ACCESS_TOKEN_KEY), AccessToken::class.java)
        } catch (@Suppress("SwallowedException") e: JsonSyntaxException) {
            null
        }
        set(value) = preferences.set(ACCESS_TOKEN_KEY, value?.let(gson::toJson))
    var refreshToken: String?
        get() = preferences.getString(REFRESH_TOKEN_KEY)
        set(value) = preferences.set(REFRESH_TOKEN_KEY, value)
    var idToken: String?
        get() = preferences.getString(ID_TOKEN_KEY)
        set(value) = preferences.set(ID_TOKEN_KEY, value)

    fun clear() {
        idToken = null
        accessToken = null
        refreshToken = null
    }

    companion object {
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
        private const val ID_TOKEN_KEY = "ID_TOKEN_KEY"
    }
}
