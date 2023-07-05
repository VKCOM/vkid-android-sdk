package com.vk.id.internal.auth.external

import android.app.Activity
import android.content.Intent
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.VKIDAuthProvider
import java.util.*

internal class VkExternalAuthProvider(
    private val appPackage: String
) : VKIDAuthProvider {
    override fun auth(activity: Activity, authOptions: AuthOptions) {
        val uri = VkExternalAuthUriBuilder()
            .uuid(authOptions.deviceId)
            .redirectUrl(authOptions.redirectUri)
            .buildForApp(appPackage)
        activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}