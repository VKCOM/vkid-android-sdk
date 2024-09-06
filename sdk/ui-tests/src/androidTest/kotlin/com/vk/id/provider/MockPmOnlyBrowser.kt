@file:OptIn(InternalVKIDApi::class)

package com.vk.id.provider

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import android.content.pm.Signature
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.context.InternalVKIDPackageManager

internal class MockPmOnlyBrowser : InternalVKIDPackageManager {

    override fun queryIntentServices(intent: Intent, flags: Int): List<ResolveInfo> = emptyList()

    override fun resolveActivity(intent: Intent, flags: Int): ResolveInfo? = null

    override fun getPackageInfo(packageName: String, flags: Int): PackageInfo {
        return if (packageName == MockChrome.PACKAGE_NAME) {
            PackageInfo().apply {
                this.packageName = MockChrome.PACKAGE_NAME
                this.versionName = MockChrome.VERSION
                this.signatures = arrayOf(Signature(MockChrome.SIGNATURE))
            }
        } else {
            PackageInfo()
        }
    }

    override fun queryIntentActivities(intent: Intent, flags: Int): List<ResolveInfo> = listOf(
        ResolveInfo().apply {
            filter = IntentFilter(Intent.ACTION_VIEW)
            filter.addCategory(Intent.CATEGORY_BROWSABLE)
            filter.addDataScheme("http")
            filter.addDataScheme("https")
            activityInfo = ActivityInfo().apply {
                this.packageName = MockChrome.PACKAGE_NAME
            }
        }
    )

    override fun resolveService(intent: Intent, flags: Int): ResolveInfo? {
        if (intent.action == "android.support.customtabs.action.CustomTabsService" &&
            intent.`package` == MockChrome.PACKAGE_NAME
        ) {
            return ResolveInfo()
        }
        return null
    }
}
