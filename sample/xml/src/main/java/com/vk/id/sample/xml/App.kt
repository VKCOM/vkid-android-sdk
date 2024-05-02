package com.vk.id.sample.xml

import android.app.Application
import com.vk.id.VKID
import com.vk.id.sample.xml.sctrictmode.StrictModeHandler

public class App : Application() {

    override fun onCreate() {
        super.onCreate()
        VKID.init(this)
        FlipperInitializer.init(this)
        if (StrictModeHandler.isStrictModeEnabled(this@App)) {
            StrictModeHandler.enableStrictMode()
        }
    }
}
