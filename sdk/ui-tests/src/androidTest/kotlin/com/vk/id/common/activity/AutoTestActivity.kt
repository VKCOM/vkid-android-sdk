package com.vk.id.common.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.ComponentActivity
import com.vk.id.common.UiTestApplication

public class AutoTestActivity : ComponentActivity() {
    public fun setContent(view: View) {
        Handler(Looper.getMainLooper()).post {
            setContentView(view)
        }
    }

    override fun startActivity(intent: Intent) {
        val mockStartActivity = (this.application as? UiTestApplication)?.mockStartActivity
        if (mockStartActivity == null) {
            super.startActivity(intent)
        } else {
            mockStartActivity.invoke(intent)
        }
    }

    override fun getPackageManager(): PackageManager = (this.application as? UiTestApplication)?.mockPackageManager ?: super.getPackageManager()
}
