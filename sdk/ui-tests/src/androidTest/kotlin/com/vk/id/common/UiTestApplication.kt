package com.vk.id.common

import android.app.Application
import android.content.pm.PackageManager

public class UiTestApplication : Application() {

    var mockPackageManager: PackageManager? = null

    override fun getPackageManager(): PackageManager = mockPackageManager ?: super.getPackageManager()
}
