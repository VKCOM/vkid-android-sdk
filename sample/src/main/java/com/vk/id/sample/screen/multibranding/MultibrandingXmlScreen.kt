package com.vk.id.sample.screen.multibranding

import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.vk.id.sample.screen.multibranding.data.multibrandingSampleData
import com.vk.id.sample.screen.multibranding.item.OAuthListWidgetItem
import com.vk.id.sample.screen.multibranding.item.createOAuthListWidgetItem
import com.vk.id.sample.uikit.item.TitleItem
import com.vk.id.sample.uikit.item.createTitleItem

@Composable
fun MultibrandingXmlScreen() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            ScrollView(context).apply {
                addView(
                    LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        multibrandingSampleData.forEach {
                            (
                                when (it) {
                                    is TitleItem -> createTitleItem(context, it.text)
                                    is OAuthListWidgetItem -> createOAuthListWidgetItem(context, it)
                                    else -> null
                                }
                                )?.let(::addView)
                        }
                    }
                )
            }
        }
    )
}
