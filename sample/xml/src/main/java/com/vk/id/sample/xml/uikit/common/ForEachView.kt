package com.vk.id.sample.xml.uikit.common

import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass

internal fun <T : View> View.forEachView(
    type: KClass<T>,
    action: (T) -> Unit
) {
    if (this::class == type) {
        @Suppress("UNCHECKED_CAST")
        action(this as T)
    }
    if (this is ViewGroup) {
        repeat(childCount) { getChildAt(it).forEachView(type, action) }
    }
}
