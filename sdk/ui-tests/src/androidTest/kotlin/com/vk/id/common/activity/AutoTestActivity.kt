package com.vk.id.common.activity

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.ComponentActivity

public class AutoTestActivity : ComponentActivity() {
    public fun setContent(view: View) {
        Handler(Looper.getMainLooper()).post {
            setContentView(view)
        }
    }
}
