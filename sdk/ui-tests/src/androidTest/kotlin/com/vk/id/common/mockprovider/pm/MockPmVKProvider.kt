@file:OptIn(InternalVKIDApi::class)

package com.vk.id.common.mockprovider.pm

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import android.content.pm.ServiceInfo
import android.content.pm.Signature
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.context.InternalVKIDPackageManager

class MockPmVKProvider : InternalVKIDPackageManager {
    override fun queryIntentServices(intent: Intent, flags: Int): List<ResolveInfo> {
        return if (intent.action == "com.vk.silentauth.action.GET_INFO") {
            listOf(
                ResolveInfo().apply {
                    serviceInfo = ServiceInfo().apply {
                        this.packageName = "com.vkontakte.android"
                        this.name = "not used"
                    }
                }
            )
        } else {
            emptyList()
        }
    }

    override fun resolveActivity(intent: Intent, flags: Int): ResolveInfo? {
        return if (intent.data.toString() == MockVK.APP_URI) {
            ResolveInfo().apply {
                activityInfo = ActivityInfo().apply {
                    packageName = MockVK.PACKAGE_NAME
                }
            }
        } else {
            null
        }
    }

    override fun getPackageInfo(packageName: String, flags: Int): PackageInfo {
        return if (packageName == MockVK.PACKAGE_NAME) {
            PackageInfo().apply {
                this.packageName = MockVK.PACKAGE_NAME
                this.signatures = arrayOf(Signature(MockVK.SIGNATURE))
            }
        } else {
            PackageInfo()
        }
    }

    override fun queryIntentActivities(intent: Intent, flags: Int): List<ResolveInfo> = emptyList()

    override fun resolveService(intent: Intent, flags: Int): ResolveInfo? {
        return if (intent.data.toString() == MockVK.APP_URI) {
            ResolveInfo().apply {
                activityInfo = ActivityInfo().apply {
                    packageName = MockVK.PACKAGE_NAME
                }
            }
        } else {
            null
        }
    }
}
