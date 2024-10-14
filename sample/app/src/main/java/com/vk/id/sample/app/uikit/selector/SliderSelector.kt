package com.vk.id.sample.app.uikit.selector

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun SliderSelector(
    title: String,
    selectedState: State<Float>,
    onStateChange: (Float) -> Unit,
) = Row(
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Slider(
        modifier = Modifier.padding(horizontal = 8.dp),
        value = selectedState.value,
        onValueChange = onStateChange,
    )
}
