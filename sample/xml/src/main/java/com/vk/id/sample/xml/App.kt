package com.vk.id.sample.xml

import android.app.Application
import com.vk.id.VKID
import com.vk.id.sample.xml.prefs.sctrictmode.StrictModeHandler

public class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FlipperInitializer.init(this)
        if (StrictModeHandler.isStrictModeEnabled(this@App)) {
            StrictModeHandler.enableStrictMode()
        }
        VKID.logsEnabled = true
        VKIDInitializer.init(this)
    }
}
