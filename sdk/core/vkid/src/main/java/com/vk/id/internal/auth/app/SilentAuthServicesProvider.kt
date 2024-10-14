@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.auth.app

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ServiceInfo
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.basicCodeFlowUri
import com.vk.id.internal.context.InternalVKIDPackageManager

internal class SilentAuthServicesProvider(
    private val packageManager: InternalVKIDPackageManager,
    private val currentPackageName: String,
    private val cache: TrustedProvidersCache
) {

    suspend fun getSilentAuthServices(): List<SilentAuthProviderData> {
        val trustedProviders = cache.getSilentAuthProviders()
        return getAppsWithSilentAuthServices()
            .asSequence()
            .mapNotNull { it.serviceInfo.mapToProviderInfo(trustedProviders) }
            .excludeCurrentApp()
            .filter { it.isAllowedToOpenWebAuth() }
            .sortedByDescending { it.weight }
            .map { SilentAuthProviderData(it.componentName, it.weight) }
            .toList()
    }

    private fun Sequence<VkAuthProviderInfo>.excludeCurrentApp() =
        filter { it.componentName.packageName != currentPackageName }

    /**
     * Method that checks if provider with specific package name is allowed to open the web auth from
     * external service (it has oauth-vk-host-impl dependency).
     */
    private fun VkAuthProviderInfo.isAllowedToOpenWebAuth(): Boolean {
        val appUri = basicCodeFlowUri(componentName.packageName)
        val appIntent = Intent(Intent.ACTION_VIEW, appUri)
        val resolveInfo = packageManager.resolveActivity(appIntent, 0)?.activityInfo
        return resolveInfo != null && resolveInfo.packageName == componentName.packageName
    }

    private fun ServiceInfo.mapToProviderInfo(
        trustedProviders: List<VkAuthSilentAuthProvider>
    ): VkAuthProviderInfo? {
        val sha = SilentAuthInfoUtils.calculateDigestHex(packageManager, packageName)
        return trustedProviders
            .firstOrNull { packageName == it.appPackage && sha == it.appSha }
            ?.let { VkAuthProviderInfo(ComponentName(packageName, name), it.weight) }
    }

    private fun getAppsWithSilentAuthServices() =
        packageManager.queryIntentServices(
            Intent(ACTION_GET_INFO),
            0
        )

    companion object {
        internal const val ACTION_GET_INFO = "com.vk.silentauth.action.GET_INFO"
    }
}
