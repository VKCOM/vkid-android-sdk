@file:OptIn(InternalVKIDApi::class)

package com.vk.id.common.mockprovider.pm

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.context.InternalVKIDPackageManager

internal class MockPmNoProvidersNoBrowsers : InternalVKIDPackageManager {
    override fun queryIntentServices(intent: Intent, flags: Int): List<ResolveInfo> = emptyList()

    override fun resolveActivity(intent: Intent, flags: Int): ResolveInfo? = null

    override fun getPackageInfo(packageName: String, flags: Int): PackageInfo = PackageInfo()

    override fun queryIntentActivities(intent: Intent, flags: Int): List<ResolveInfo> = emptyList()

    override fun resolveService(intent: Intent, flags: Int): ResolveInfo? = null
}
