package com.vk.id.common.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.ComponentActivity
import com.vk.id.common.App

public class AutoTestActivity : ComponentActivity() {
    public fun setContent(view: View) {
        Handler(Looper.getMainLooper()).post {
            setContentView(view)
        }
    }

    var mockPackageManager: PackageManager? = null
        set(value) {
            field = value
            (this.application as App).mockPackageManager = value
        }

    var mockStartActivity: ((Intent) -> Unit)? = null
        set(value) {
            field = value
            (this.application as App).mockStartActivity = value
        }

    override fun startActivity(intent: Intent) {
        if (mockStartActivity == null) {
            super.startActivity(intent)
        } else {
            mockStartActivity?.invoke(intent)
        }
    }

    override fun getPackageManager(): PackageManager = mockPackageManager ?: super.getPackageManager()
}
