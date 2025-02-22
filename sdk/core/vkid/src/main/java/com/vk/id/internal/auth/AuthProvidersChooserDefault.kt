@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.auth

import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.analytics.VKIDAnalytics.EventParam
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.app.AppAuthProvider
import com.vk.id.internal.auth.app.SilentAuthServicesProvider
import com.vk.id.internal.auth.web.WebAuthProvider
import com.vk.id.internal.context.InternalVKIDActivityStarter
import com.vk.id.internal.context.InternalVKIDPackageManager
import com.vk.id.logger.internalVKIDCreateLoggerForClass

internal class AuthProvidersChooserDefault(
    private val packageManager: InternalVKIDPackageManager,
    private val silentAuthServicesProvider: SilentAuthServicesProvider,
    private val activityStarter: InternalVKIDActivityStarter
) : AuthProvidersChooser {
    private val logger = internalVKIDCreateLoggerForClass()
    override suspend fun chooseBest(params: VKIDAuthParams): VKIDAuthProvider {
        if (!params.useOAuthProviderIfPossible || params.oAuth != null) {
            VKIDAnalytics.trackEvent("no_auth_provider", EventParam(name = "sdk_type", strValue = "vkid"))
            return WebAuthProvider(packageManager, activityStarter)
        }
        val authProvider = silentAuthServicesProvider.getSilentAuthServices()
            .maxByOrNull { it.weight }
            ?.componentName
            ?.packageName
            ?.let {
                logger.debug("Silent auth provider found: $it")
                AppAuthProvider(activityStarter, it)
            }
        return if (authProvider == null) {
            VKIDAnalytics.trackEvent("no_auth_provider", EventParam(name = "sdk_type", strValue = "vkid"))
            WebAuthProvider(packageManager, activityStarter)
        } else {
            VKIDAnalytics.trackEvent("auth_provider_used", EventParam(name = "sdk_type", strValue = "vkid"))
            authProvider
        }
    }
}
