package com.vk.id.sample.xml

import android.app.Application
import android.content.Context
import com.vk.id.VKID

public class App : Application() {
    public val vkid: VKID by lazy { VKID(this) }

    override fun onCreate() {
        super.onCreate()
        FlipperInitializer.init(this)
    }
}

public val Context.vkid: VKID get() = (this.applicationContext as App).vkid
