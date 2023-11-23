package com.vk.id.sample.uikit.item

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.sample.uikit.common.darkBackground

data class HalfSpacerItem(
    val isDarkBackground: Boolean = false
)

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
