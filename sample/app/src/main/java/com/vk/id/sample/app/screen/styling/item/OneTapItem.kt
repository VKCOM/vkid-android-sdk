package com.vk.id.sample.app.screen.styling.item

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.AccessToken
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.sample.xml.onetap.item.OneTapItem
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback

@Composable
fun HandleOneTapItem(
    context: Context,
    item: Any,
    onToken: (AccessToken) -> Unit
) {
    if (item !is OneTapItem) return
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        OneTap(
            modifier = Modifier.width(item.width.dp),
            style = item.style,
            onAuth = getOneTapSuccessCallback(context, onToken),
            onFail = getOneTapFailCallback(context),
            oAuths = item.oAuths,
            signInAnotherAccountButtonEnabled = true
        )
    }
}
