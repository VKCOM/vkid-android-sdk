package com.vk.id.sample.xml.onetap

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vk.id.onetap.xml.OneTap
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.forEachView
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback
import com.vk.id.sample.xml.uikit.common.showToast

public class OneTapStylingXmlLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vkid_activity_one_tap)
        findViewById<View>(android.R.id.content).rootView.forEachView(OneTap::class) { widget ->
            widget.setCallbacks(
                onAuth = getOneTapSuccessCallback(this) {},
                onFail = getOneTapFailCallback(this),
            )
            widget.snackbarHost = findViewById(R.id.group_subscription_snackbar_host)
            widget.groupId = "1"
            widget.setGroupSubscriptionCallbacks(
                onSuccess = { showToast(this, "Subscribed") },
                onFail = { showToast(this, "Fail: ${it.description}") },
            )
        }
    }
}
