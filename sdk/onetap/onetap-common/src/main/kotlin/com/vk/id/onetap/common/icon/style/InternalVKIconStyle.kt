package com.vk.id.onetap.common.icon.style

import androidx.compose.runtime.Immutable
import com.vk.id.common.InternalVKIDApi

@Immutable
@InternalVKIDApi
@Suppress("ForbiddenPublicDataClass")
public data class InternalVKIconStyle(
    public val colorStyle: InternalVKIconColorStyle,
    public val sizeStyle: InternalVKIconSizeStyle,
)
