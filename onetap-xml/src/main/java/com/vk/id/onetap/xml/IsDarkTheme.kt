package com.vk.id.onetap.xml

import android.content.Context
import android.content.res.Configuration

internal val Context.isDarkTheme: Boolean
    get() = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
