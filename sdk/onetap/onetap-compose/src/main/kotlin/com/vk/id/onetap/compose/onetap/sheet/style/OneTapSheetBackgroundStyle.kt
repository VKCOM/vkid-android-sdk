package com.vk.id.onetap.compose.onetap.sheet.style

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import com.vk.id.onetap.compose.R

internal enum class OneTapSheetBackgroundStyle {
    LIGHT,
    DARK,
}

@Suppress("ModifierComposed")
internal fun Modifier.background(style: OneTapSheetBackgroundStyle) = composed {
    val backgroundResource = when (style) {
        OneTapSheetBackgroundStyle.LIGHT -> R.color.vkid_white
        OneTapSheetBackgroundStyle.DARK -> R.color.vkid_background_modal_inverse
    }
    background(colorResource(backgroundResource))
}
