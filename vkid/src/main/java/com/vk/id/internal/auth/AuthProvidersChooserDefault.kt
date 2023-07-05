package com.vk.id.internal.auth

import com.vk.id.internal.auth.browser.AuthBrowser
import com.vk.id.internal.auth.external.SilentAuthServicesProvider
import com.vk.id.internal.auth.external.VkExternalAuthProvider

internal class AuthProvidersChooserDefault(
    private val silentAuthServicesProvider: SilentAuthServicesProvider
) : AuthProvidersChooser {
    override fun chooseBest(): VKIDAuthProvider {
        return silentAuthServicesProvider.getSilentAuthServices().firstOrNull()?.packageName
            ?.let { VkExternalAuthProvider(it) }
            ?: AuthBrowser()
    }
}
