@file:OptIn(InternalVKIDApi::class)

package com.vk.id.storage

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.vk.id.AccessToken
import com.vk.id.RefreshToken
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public class InternalVKIDTokenStorage(
    private val preferences: InternalVKIDPreferencesStorage
) {
    private val gson = Gson()
    internal var accessToken: AccessToken?
        get() = try {
            gson.fromJson(preferences.getString(ACCESS_TOKEN_KEY), AccessToken::class.java)
        } catch (@Suppress("SwallowedException") e: JsonSyntaxException) {
            null
        }
        set(value) = preferences.set(ACCESS_TOKEN_KEY, value?.let(gson::toJson))

    public val currentAccessToken: AccessToken? get() = accessToken

    internal var refreshToken: RefreshToken?
        get() = try {
            preferences.getString(REFRESH_TOKEN_V2_KEY)?.let { gson.fromJson(it, RefreshToken::class.java) }
                ?: preferences.getString(REFRESH_TOKEN_V1_KEY)?.let { RefreshToken(token = it, scopes = null) }
        } catch (@Suppress("SwallowedException") e: JsonSyntaxException) {
            null
        }
        set(value) = preferences.set(REFRESH_TOKEN_V2_KEY, value?.let(gson::toJson))

    internal fun clear() {
        accessToken = null
        refreshToken = null
        preferences.set(REFRESH_TOKEN_V1_KEY, null)
    }

    internal companion object {
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
        private const val REFRESH_TOKEN_V1_KEY = "REFRESH_TOKEN_KEY"
        private const val REFRESH_TOKEN_V2_KEY = "REFRESH_TOKEN_WITH_SCOPES_KEY"
    }
}
