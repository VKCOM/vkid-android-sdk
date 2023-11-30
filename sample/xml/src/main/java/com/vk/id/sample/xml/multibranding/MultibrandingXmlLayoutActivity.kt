package com.vk.id.sample.xml.multibranding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vk.id.multibranding.xml.OAuthListWidget
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.multibranding.util.getOAuthListCallback
import com.vk.id.sample.xml.uikit.common.forEachView
import com.vk.id.sample.xml.uikit.common.onVKIDAuthFail

public class MultibrandingXmlLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vkid_activity_multibranding)
        findViewById<View>(android.R.id.content).rootView.forEachView(OAuthListWidget::class) { widget ->
            widget.setCallbacks(
                onAuth = getOAuthListCallback(this, {}),
                onFail = { oAuth, fail -> onVKIDAuthFail(this, fail) }, // TODO: Handle oAuth
            )
        }
    }
}
