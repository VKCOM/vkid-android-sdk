package com.vk.id.sample.screen.styling.item

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.button.VKIDButtonStyle
import com.vk.id.sample.screen.styling.util.onVKIDAuthFail
import com.vk.id.sample.screen.styling.util.onVKIDAuthSuccess
import com.vk.id.sample.uikit.common.darkBackground

data class ButtonItem(
    val style: VKIDButtonStyle,
    val width: Int = 335,
    val isDarkBackground: Boolean = false,
)

@Composable
fun HandleButtonItem(
    context: Context,
    item: Any
) {
    if (item !is ButtonItem) return
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .darkBackground(item.isDarkBackground)
            .fillMaxWidth()
    ) {
        OneTap(
            modifier = Modifier.width(item.width.dp),
            style = item.style,
            onAuth = { onVKIDAuthSuccess(context, it) },
            onFail = { onVKIDAuthFail(context, it) },
            signInAnotherAccountButtonEnabled = true
        )
    }
}
