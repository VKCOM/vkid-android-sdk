package com.vk.id.onetap.common.icon.style

import androidx.compose.runtime.Immutable
import com.vk.id.commn.InternalVKIDApi

@Immutable
@InternalVKIDApi
public data class VKIconStyle(
    public val colorStyle: VKIconColorStyle,
    public val sizeStyle: VKIconSizeStyle,
)
