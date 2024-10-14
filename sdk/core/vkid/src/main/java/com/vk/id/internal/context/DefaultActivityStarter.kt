package com.vk.id.internal.context

import android.content.Context
import android.content.Intent
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.AuthActivity

@OptIn(InternalVKIDApi::class)
internal class DefaultActivityStarter(private val context: Context) : InternalVKIDActivityStarter {
    override fun startActivity(intent: Intent) {
        AuthActivity.startForAuth(context, intent)
    }
}
