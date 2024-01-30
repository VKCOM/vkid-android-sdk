package com.vk.id.sample.xml.multibranding

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.multibranding.data.multibrandingSampleData
import com.vk.id.sample.xml.multibranding.item.OAuthListWidgetItem
import com.vk.id.sample.xml.multibranding.item.createOAuthListWidgetItem
import com.vk.id.sample.xml.uikit.item.TitleItem
import com.vk.id.sample.xml.uikit.item.TitleItemView

public class MultibrandingXmlCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ScrollView(this).apply {
                addView(
                    LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        multibrandingSampleData(context).forEach {
                            (
                                when (it) {
                                    is TitleItem -> TitleItemView.create(context, it.text)
                                    is OAuthListWidgetItem -> createOAuthListWidgetItem(context, it)
                                    else -> null
                                }
                                )?.let(::addView)
                        }
                    }
                )
            }
        )
        findViewById<View>(android.R.id.content).setBackgroundResource(R.color.vkid_background)
    }
}
