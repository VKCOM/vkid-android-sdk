package com.vk.id.sample.xml.groupsubscription

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vk.id.VKID
import com.vk.id.group.subscription.xml.GroupSubscriptionSheet
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.forEachView
import com.vk.id.sample.xml.uikit.common.showToast

public class GroupSubscriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vkid_activity_group_subscription)
        findViewById<View>(android.R.id.content).rootView.forEachView(GroupSubscriptionSheet::class) { widget ->
            widget.accessTokenProvider = {
                VKID.instance.accessToken?.token ?: run {
                    showToast(this, "Not authorized")
                    ""
                }
            }
            widget.setCallbacks(
                onSuccess = { showToast(this, "Success") },
                onFail = { showToast(this, "Fail") },
            )
            widget.snackbarHost = findViewById(R.id.group_subscription_snackbar_host)
            findViewById<View>(R.id.show_sheet_button).setOnClickListener {
                widget.show()
            }
        }
    }
}
