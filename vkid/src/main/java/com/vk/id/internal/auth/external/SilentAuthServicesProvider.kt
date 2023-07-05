package com.vk.id.internal.auth.external

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo

internal class SilentAuthServicesProvider(
    private val context: Context,
    private val cache: TrustedProvidersCache
) {

    fun getSilentAuthServices(): List<ComponentName> {
        val trustedProviders = cache.getSilentAuthProviders()
        return getAppsWithSilentAuthServices()
            .asSequence()
            .mapNotNull { it.serviceInfo.mapToProviderInfo(trustedProviders) }
            .excludeCurrentApp()
            .filter { it.isAllowedToOpenWebAuth() }
            .sortedByDescending { it.weight }
            .map { it.componentName }
            .toList()
    }

    private fun Sequence<VkExternalAuthProviderInfo>.excludeCurrentApp() = filter { it.componentName.packageName != context.packageName }

    /**
     * Method that checks if provider with specific package name is allowed to open the web auth from
     * external service (it has oauth-vk-host-impl dependency).
     */
    private fun VkExternalAuthProviderInfo.isAllowedToOpenWebAuth(): Boolean {
        val appUri = VkExternalAuthUriBuilder.buildAppBaseUri(componentName.packageName)
        val appIntent = Intent(Intent.ACTION_VIEW, appUri)
        val resolveInfo = context.packageManager.resolveActivity(appIntent, 0)?.activityInfo
        return resolveInfo != null && resolveInfo.packageName == componentName.packageName
    }

    private fun ServiceInfo.mapToProviderInfo(
        trustedProviders: List<VkAuthSilentAuthProvider>
    ): VkExternalAuthProviderInfo? {
        val sha = SilentAuthInfoUtils.calculateDigestHex(context, packageName)
        return trustedProviders
            .firstOrNull { packageName == it.appPackage && sha == it.appSha }
            ?.let { VkExternalAuthProviderInfo(ComponentName(packageName, name), it.weight) }
    }

    private fun getAppsWithSilentAuthServices() = context.applicationContext.packageManager.queryIntentServices(Intent(ACTION_GET_INFO), 0)

    companion object {
        private const val ACTION_GET_INFO = "com.vk.silentauth.action.GET_INFO"
    }
}