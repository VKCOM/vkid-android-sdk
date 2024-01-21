package com.vk.id.sample.app.uikit.selector

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Suppress("NonSkippableComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T : Any> DropdownSelector(
    modifier: Modifier = Modifier,
    values: Map<String, T>,
    selectedValue: String,
    onValueSelected: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { }
        ) {
            values.entries.forEachIndexed { i, (title, item) ->
                DropdownMenuItem(
                    text = {
                        Text(text = title)
                    },
                    onClick = {
                        onValueSelected(item)
                        expanded = false
                    }
                )
                if (i != values.size - 1) {
                    Divider()
                }
            }
        }
    }
}
