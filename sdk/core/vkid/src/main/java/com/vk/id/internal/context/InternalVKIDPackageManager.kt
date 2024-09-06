package com.vk.id.internal.context

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface InternalVKIDPackageManager {
    fun queryIntentServices(intent: Intent, flags: Int): List<ResolveInfo>
    fun resolveActivity(intent: Intent, flags: Int): ResolveInfo?
    fun getPackageInfo(packageName: String, flags: Int): PackageInfo
    fun queryIntentActivities(intent: Intent, flags: Int): List<ResolveInfo>
    fun resolveService(intent: Intent, flags: Int): ResolveInfo?
}
