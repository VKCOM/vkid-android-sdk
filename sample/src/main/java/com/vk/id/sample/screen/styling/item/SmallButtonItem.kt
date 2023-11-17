package com.vk.id.sample.screen.styling.item

import android.content.Context
import androidx.compose.runtime.Composable
import com.vk.id.onetap.compose.button.VKIDButtonSmall
import com.vk.id.onetap.compose.button.VKIDButtonStyle
import com.vk.id.sample.screen.styling.util.onVKIDAuthFail
import com.vk.id.sample.screen.styling.util.onVKIDAuthSuccess

data class SmallButtonItem(
    val style: VKIDButtonStyle
)

@Composable
fun HandleSmallButtonItem(
    context: Context,
    item: Any
) {
    if (item !is SmallButtonItem) return
    VKIDButtonSmall(
        style = item.style,
        onAuth = { onVKIDAuthSuccess(context, it) },
        onFail = { onVKIDAuthFail(context, it) }
    )
}
