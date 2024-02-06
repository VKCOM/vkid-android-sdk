package com.vk.id.sample.xml.uikit.common

import android.content.Context
import android.content.res.Configuration

public val Context.isDarkTheme: Boolean
    get() = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
