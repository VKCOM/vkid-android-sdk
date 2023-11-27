package com.vk.id.sample.xml.onetap

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vk.id.onetap.xml.OneTap
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.forEachView
import com.vk.id.sample.xml.uikit.common.onVKIDAuthFail
import com.vk.id.sample.xml.uikit.common.onVKIDAuthSuccess

public class OneTapStylingXmlLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vkid_activity_one_tap)
        findViewById<View>(android.R.id.content).rootView.forEachView(OneTap::class) { widget ->
            widget.setCallbacks(
                onAuth = { onVKIDAuthSuccess(this, it) },
                onFail = { onVKIDAuthFail(this, it) },
            )
        }
    }
}
