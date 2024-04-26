package com.vk.id.sample.xml

import android.app.Application
import android.content.Context
import com.vk.id.VKID
import com.vk.id.sample.xml.sctrictmode.StrictModeHandler

public class App : Application() {
    public val vkid: VKID by lazy { VKID(this) }

    override fun onCreate() {
        super.onCreate()
        FlipperInitializer.init(this)
        if (StrictModeHandler.isStrictModeEnabled(this@App)) {
            StrictModeHandler.enableStrictMode()
        }
    }
}

public val Context.vkid: VKID get() = (this.applicationContext as App).vkid
