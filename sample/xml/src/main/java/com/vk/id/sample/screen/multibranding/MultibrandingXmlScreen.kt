package com.vk.id.sample.screen.multibranding

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.vk.id.sample.screen.multibranding.data.multibrandingSampleData
import com.vk.id.sample.screen.multibranding.item.OAuthListWidgetItem
import com.vk.id.sample.screen.multibranding.item.createOAuthListWidgetItem
import com.vk.id.sample.uikit.item.TitleItem
import com.vk.id.sample.uikit.item.createTitleItem

//@Composable
//fun MultibrandingXmlScreen() {
//    AndroidView(
//        modifier = Modifier.fillMaxSize(),
//        factory = { context ->
//            ScrollView(context).apply {
//                addView(
//                    LinearLayout(context).apply {
//                        orientation = LinearLayout.VERTICAL
//                        multibrandingSampleData.forEach {
//                            (
//                                when (it) {
//                                    is TitleItem -> createTitleItem(context, it.text)
//                                    is OAuthListWidgetItem -> createOAuthListWidgetItem(context, it)
//                                    else -> null
//                                }
//                                )?.let(::addView)
//                        }
//                    }
//                )
//            }
//        }
//    )
//}
public class MultibrandingXmlScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ScrollView(this).apply {
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
        )
    }
}
