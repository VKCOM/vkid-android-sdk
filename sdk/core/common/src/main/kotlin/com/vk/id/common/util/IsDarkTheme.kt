package com.vk.id.common.util

import android.content.Context
import android.content.res.Configuration
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public val Context.internalVkIdIsDarkTheme: Boolean
    get() = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
