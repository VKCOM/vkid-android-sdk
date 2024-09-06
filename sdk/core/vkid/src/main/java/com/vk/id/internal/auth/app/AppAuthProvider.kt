@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.auth.app

import android.content.Intent
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.auth.toAuthUriCodeFlow
import com.vk.id.internal.context.InternalVKIDActivityStarter

internal class AppAuthProvider(
    private val starter: InternalVKIDActivityStarter,
    private val appPackage: String
) : VKIDAuthProvider {
    override fun auth(authOptions: AuthOptions) {
        val uri = authOptions.toAuthUriCodeFlow(appPackage)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        starter.startActivity(intent)
    }
}
