package com.vk.id.sample.app.uikit.item

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.sample.app.uikit.common.darkBackground
import com.vk.id.sample.uikit.item.HalfSpacerItem

@Composable
fun HandleHalfSpacerItem(item: Any) {
    if (item !is HalfSpacerItem) return
    Spacer(
        modifier = Modifier
            .darkBackground(item.isDarkBackground)
            .height(12.dp)
            .fillMaxWidth(),
    )
}
