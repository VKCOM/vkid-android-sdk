@file:OptIn(InternalVKIDApi::class)

package com.vk.id.sample.xml

import android.app.Application
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.sample.xml.flutter.IsFlutterHandler
import com.vk.id.sample.xml.sctrictmode.StrictModeHandler

public class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FlipperInitializer.init(this)
        if (StrictModeHandler.isStrictModeEnabled(this@App)) {
            StrictModeHandler.enableStrictMode()
        }
        VKID.logsEnabled = true
        VKID.init(this, IsFlutterHandler.isFlutter(this))
    }
}
