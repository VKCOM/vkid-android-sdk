package com.vk.id.sample.uikit.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.sample.uikit.common.darkBackground

data class SpacerItem(
    val isDarkBackground: Boolean = false
)

@Composable
fun HandleSpacerItem(item: Any) {
    if (item !is SpacerItem) return
    Spacer(
        modifier = Modifier
            .darkBackground(item.isDarkBackground)
            .height(24.dp)
            .fillMaxWidth(),
    )
}
