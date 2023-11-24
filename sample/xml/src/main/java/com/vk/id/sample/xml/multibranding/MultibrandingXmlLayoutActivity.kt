package com.vk.id.sample.xml.multibranding

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.vk.id.multibranding.xml.OAuthListWidget
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.multibranding.util.getOAuthListCallback
import com.vk.id.sample.xml.uikit.common.onVKIDAuthFail

public class MultibrandingXmlLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vkid_activity_multibranding)
        forEachOAuthListWidget(findViewById<View>(android.R.id.content).rootView) { widget ->
            widget.setCallbacks(
                onAuth = getOAuthListCallback(this),
                onFail = { onVKIDAuthFail(this, it) },
            )
        }
    }

    private fun forEachOAuthListWidget(view: View, action: (OAuthListWidget) -> Unit) {
        if (view is OAuthListWidget) {
            action(view)
        }
        if (view is ViewGroup) {
            repeat(view.childCount) { forEachOAuthListWidget(view.getChildAt(it), action) }
        }
    }
}
