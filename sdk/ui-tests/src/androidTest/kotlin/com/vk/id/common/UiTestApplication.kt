package com.vk.id.common

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager

public class UiTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    var mockPackageManager: PackageManager? = null
    var mockStartActivity: ((Intent) -> Unit)? = null

    override fun getPackageManager(): PackageManager = mockPackageManager ?: super.getPackageManager()

    override fun startActivity(intent: Intent) {
        if (mockStartActivity == null) {
            super.startActivity(intent)
        } else {
            mockStartActivity?.invoke(intent)
        }
    }
}
