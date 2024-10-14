package com.vk.id.sample.app.uikit.selector

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import kotlin.enums.enumEntries

@Composable
internal inline fun <reified T : Enum<T>> EnumStateCheckboxSelector(
    state: State<Set<T>>,
    crossinline onNewState: (Set<T>) -> Unit,
) {
    enumEntries<T>().forEach { value ->
        CheckboxSelector(
            title = value.name,
            isChecked = state.value.contains(value),
            onCheckedChange = {
                if (it) {
                    onNewState(state.value + value)
                } else {
                    onNewState(state.value - value)
                }
            }
        )
    }
}

@Composable
internal fun CheckboxSelector(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) = Row(
    verticalAlignment = Alignment.CenterVertically
) {
    Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
    )
}
