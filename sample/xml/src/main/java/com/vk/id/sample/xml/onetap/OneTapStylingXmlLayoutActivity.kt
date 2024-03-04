package com.vk.id.sample.xml.onetap

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vk.id.onetap.xml.OneTap
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.forEachView
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback
import com.vk.id.sample.xml.vkid

public class OneTapStylingXmlLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vkid_activity_one_tap)
        findViewById<View>(android.R.id.content).rootView.forEachView(OneTap::class) { widget ->
            widget.setVKID(vkid)
            widget.setCallbacks(
                onAuth = getOneTapSuccessCallback(this) {},
                onFail = getOneTapFailCallback(this),
            )
        }
    }
}
