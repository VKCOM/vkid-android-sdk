package com.vk.id.internal.auth

import com.vk.id.internal.auth.web.WebAuthProvider
import com.vk.id.internal.auth.app.SilentAuthServicesProvider
import com.vk.id.internal.auth.app.AppAuthProvider
import com.vk.id.internal.log.createLoggerForClass

internal class AuthProvidersChooserDefault(
    private val silentAuthServicesProvider: SilentAuthServicesProvider
) : AuthProvidersChooser {
    private val logger = createLoggerForClass()
    override fun chooseBest(): VKIDAuthProvider {
        return silentAuthServicesProvider.getSilentAuthServices().firstOrNull()?.packageName
            ?.let {
                logger.debug("Silent auth provider found: $it")
                AppAuthProvider(it)
            } ?: WebAuthProvider()
    }
}
