package com.vk.id.internal.context

import android.content.Intent
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface InternalVKIDActivityStarter {
    fun startActivity(intent: Intent)
}
