package com.vk.id.internal.auth.app

import android.content.Context
import android.content.Intent
import com.vk.id.internal.auth.AuthActivity
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.auth.toAuthUriCodeFlow

internal class AppAuthProvider(
    private val context: Context,
    private val appPackage: String
) : VKIDAuthProvider {
    override fun auth(authOptions: AuthOptions) {
        val uri = authOptions.toAuthUriCodeFlow(appPackage)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        AuthActivity.startForAuth(context, intent)
    }
}
