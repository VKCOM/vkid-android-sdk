package com.vk.id.sample.xml.onetap

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.vk.id.onetap.xml.OneTap
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.forEachView
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback
import com.vk.id.sample.xml.uikit.common.showToast

public class OneTapStylingXmlLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.vkid_activity_one_tap)
        findViewById<ScrollView>(R.id.onetap_content).apply {
            ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
                val insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
                )
                view.updatePadding(
                    left = insets.left,
                    top = insets.top,
                    right = insets.right,
                    bottom = insets.bottom,
                )
                windowInsets
            }
            ViewCompat.requestApplyInsets(this)
        }
        findViewById<View>(android.R.id.content).rootView.forEachView(OneTap::class) { widget ->
            widget.setCallbacks(
                onAuth = getOneTapSuccessCallback(this) {},
                onFail = getOneTapFailCallback(this),
            )
            widget.snackbarHost = findViewById(R.id.group_subscription_snackbar_host)
            widget.setGroupSubscriptionCallbacks(
                onSuccess = { showToast(this, "Subscribed") },
                onFail = { showToast(this, "Fail: ${it.description}") },
            )
        }
    }
}
