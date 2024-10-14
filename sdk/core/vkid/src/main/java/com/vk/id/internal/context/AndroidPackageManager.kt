package com.vk.id.internal.context

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.vk.id.common.InternalVKIDApi

@OptIn(InternalVKIDApi::class)
internal class AndroidPackageManager(private val packageManager: PackageManager) : InternalVKIDPackageManager {
    @SuppressLint("QueryPermissionsNeeded") // Registered in manifest
    override fun queryIntentServices(
        intent: Intent,
        flags: Int
    ): List<ResolveInfo> {
        return packageManager.queryIntentServices(intent, flags)
    }

    @SuppressLint("QueryPermissionsNeeded") // Registered in manifest
    override fun queryIntentActivities(
        intent: Intent,
        flags: Int
    ): List<ResolveInfo> = packageManager.queryIntentActivities(intent, flags)

    override fun resolveActivity(intent: Intent, flags: Int) = packageManager.resolveActivity(intent, flags)

    override fun getPackageInfo(
        packageName: String,
        flags: Int
    ): PackageInfo = packageManager.getPackageInfo(packageName, flags)

    override fun resolveService(intent: Intent, flags: Int): ResolveInfo? = packageManager.resolveService(intent, flags)
}
