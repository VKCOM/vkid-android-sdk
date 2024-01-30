package com.vk.id.commn.util

import android.content.Context
import android.content.res.Configuration
import com.vk.id.commn.InternalVKIDApi

@InternalVKIDApi
public val Context.isDarkTheme: Boolean
    get() = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
