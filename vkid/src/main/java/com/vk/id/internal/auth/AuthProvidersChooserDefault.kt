package com.vk.id.internal.auth

import com.vk.id.internal.auth.browser.AuthBrowser
import com.vk.id.internal.auth.external.SilentAuthServicesProvider
import com.vk.id.internal.auth.external.VkExternalAuthProvider
import com.vk.id.internal.log.createLoggerForClass

internal class AuthProvidersChooserDefault(
    private val silentAuthServicesProvider: SilentAuthServicesProvider
) : AuthProvidersChooser {
    private val logger = createLoggerForClass()
    override fun chooseBest(): VKIDAuthProvider {
        return silentAuthServicesProvider.getSilentAuthServices().firstOrNull()?.packageName
            ?.let {
                logger.debug("Silent auth provider found: $it")
                VkExternalAuthProvider(it)
            } ?: AuthBrowser()
    }
}
