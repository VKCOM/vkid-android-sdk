package com.vk.id.sample.xml.onetapsheet

import android.os.Bundle
import android.widget.Button
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.vk.id.onetap.xml.OneTapBottomSheet
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback
import com.vk.id.sample.xml.uikit.common.showToast

public class OnetapSheetStylingXmlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vkid_activity_onetap_bottom_sheet)
        setupSheet(R.id.show_sheet_button, R.id.vkid_bottom_sheet)
        setupSheet(R.id.show_sheet_without_fast_auth_button, R.id.vkid_bottom_sheet_without_fast_auth)
    }

    private fun setupSheet(@IdRes buttonId: Int, @IdRes bottomSheetId: Int) {
        val vkidOneTapBottomSheet = findViewById<OneTapBottomSheet>(bottomSheetId)
        vkidOneTapBottomSheet.setCallbacks(
            onAuth = getOneTapSuccessCallback(this) {},
            onFail = getOneTapFailCallback(this),
        )
        findViewById<Button>(buttonId).setOnClickListener { vkidOneTapBottomSheet.show() }
        vkidOneTapBottomSheet.snackbarHost = findViewById(R.id.group_subscription_snackbar_host)
        vkidOneTapBottomSheet.groupId = "1"
        vkidOneTapBottomSheet.setGroupSubscriptionCallbacks(
            onSuccess = { showToast(this, "Subscribed") },
            onFail = { showToast(this, "Fail: ${it.description}") },
        )
    }
}
