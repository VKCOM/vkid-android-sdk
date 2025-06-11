@file:OptIn(InternalVKIDApi::class)

package com.vk.id.storage

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface InternalVKIDPreferencesStorage {
    public fun set(key: String, value: String?)
    public fun getString(key: String): String?
}

internal class InternalVKIDEncryptedSharedPreferencesStorage public constructor(
    context: Context
) : InternalVKIDPreferencesStorage {

    private companion object {
        const val FILE_NAME = "vkid_encrypted_shared_prefs"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        try {
            createSharedPreferences(context)
        } catch (@Suppress("TooGenericExceptionCaught", "SwallowedException") t: Throwable) {
            // https://github.com/tink-crypto/tink-java/issues/23
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.deleteSharedPreferences(FILE_NAME)
            }
            createSharedPreferences(context)
        }
    }

    override fun set(key: String, value: String?) {
        sharedPreferences.edit(commit = true) { putString(key, value) }
    }

    override fun getString(key: String): String? = sharedPreferences.getString(key, null)

    private fun createSharedPreferences(context: Context) = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
