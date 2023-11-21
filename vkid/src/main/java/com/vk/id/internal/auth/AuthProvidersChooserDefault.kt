package com.vk.id.internal.auth

import com.vk.id.auth.VKIDAuthParams
import com.vk.id.internal.auth.app.AppAuthProvider
import com.vk.id.internal.auth.app.SilentAuthServicesProvider
import com.vk.id.internal.auth.web.WebAuthProvider
import com.vk.id.internal.log.createLoggerForClass

internal class AuthProvidersChooserDefault(
    private val silentAuthServicesProvider: SilentAuthServicesProvider
) : AuthProvidersChooser {
    private val logger = createLoggerForClass()
    override suspend fun chooseBest(params: VKIDAuthParams): VKIDAuthProvider {
        if (!params.useExistingUserIfPossible || params.oAuth != null) {
            return WebAuthProvider()
        }
        return silentAuthServicesProvider.getSilentAuthServices()
            .maxByOrNull { it.weight }
            ?.componentName
            ?.packageName
            ?.let {
                logger.debug("Silent auth provider found: $it")
                AppAuthProvider(it)
            } ?: WebAuthProvider()
    }
}
