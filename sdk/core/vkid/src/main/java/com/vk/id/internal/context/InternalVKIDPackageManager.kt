package com.vk.id.internal.context

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface InternalVKIDPackageManager {
    public fun queryIntentServices(intent: Intent, flags: Int): List<ResolveInfo>
    public fun resolveActivity(intent: Intent, flags: Int): ResolveInfo?
    public fun getPackageInfo(packageName: String, flags: Int): PackageInfo
    public fun queryIntentActivities(intent: Intent, flags: Int): List<ResolveInfo>
    public fun resolveService(intent: Intent, flags: Int): ResolveInfo?
}
