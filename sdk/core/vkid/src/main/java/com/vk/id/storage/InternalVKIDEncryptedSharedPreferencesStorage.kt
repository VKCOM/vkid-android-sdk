package com.vk.id.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public class InternalVKIDEncryptedSharedPreferencesStorage internal constructor(
    context: Context
) {
    private val sharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            "encrypted_shared_prefs",
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    public fun set(key: String, value: String?): Unit = sharedPreferences.edit().putString(key, value).apply()

    public fun getString(key: String): String? = sharedPreferences.getString(key, null)
}
