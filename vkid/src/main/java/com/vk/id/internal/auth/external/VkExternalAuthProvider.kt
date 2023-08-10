package com.vk.id.internal.auth.external

import android.app.Activity
import android.content.Intent
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.auth.browser.OAuthActivity
import com.vk.id.internal.auth.toAuthUriCodeFlow

internal class VkExternalAuthProvider(
    private val appPackage: String
) : VKIDAuthProvider {
    override fun auth(activity: Activity, authOptions: AuthOptions) {
        val uri = authOptions.toAuthUriCodeFlow(appPackage)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        OAuthActivity.startForAuth(activity, intent)
    }
}