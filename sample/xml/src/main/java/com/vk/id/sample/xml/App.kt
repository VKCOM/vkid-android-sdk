package com.vk.id.sample.xml

import android.app.Application
import android.content.Context
import com.vk.id.VKID

public class App : Application() {
    public val vkid: VKID by lazy { VKID(this) }
}

public val Context.vkid: VKID get() = (this.applicationContext as App).vkid
