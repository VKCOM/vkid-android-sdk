package com.vk.id.sample.app.screen.styling.item

import android.content.Context
import androidx.compose.runtime.Composable
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapStyle
import com.vk.id.sample.app.screen.styling.util.onVKIDAuthFail
import com.vk.id.sample.app.screen.styling.util.onVKIDAuthSuccess

data class IconButtonItem(
    val style: OneTapStyle
)

@Composable
fun HandleSmallButtonItem(
    context: Context,
    item: Any
) {
    if (item !is IconButtonItem) return
    OneTap(
        style = item.style,
        onAuth = { onVKIDAuthSuccess(context, it) },
        onFail = { onVKIDAuthFail(context, it) }
    )
}
