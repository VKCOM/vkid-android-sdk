package com.vk.id.test

import android.content.Context
import android.content.Intent
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.AuthActivity

@InternalVKIDApi
public object InternalVKIDAuthStarter {
    public fun startAuth(context: Context, intent: Intent) {
        AuthActivity.startForAuth(context, intent)
    }
}
