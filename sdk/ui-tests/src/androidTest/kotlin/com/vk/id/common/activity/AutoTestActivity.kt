package com.vk.id.common.activity

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

    override fun getPackageManager(): PackageManager = (this.application as? UiTestApplication)?.mockPackageManager ?: super.getPackageManager()
}
