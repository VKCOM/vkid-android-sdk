package com.vk.id.sample.app.uikit.selector

import android.content.Context
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
import com.vk.id.sample.app.util.carrying.carry
import java.util.Locale
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createType
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor

internal fun <T : Any> KClass<T>.styleConstructors(
    context: Context
) = simpleStyleConstructors() + systemStyleConstructors(context)

internal fun <T : Any> KClass<T>.simpleStyleConstructors() = sealedSubclasses
    .associate { it.simpleName!! to it.primaryConstructor!! }

private fun <T : Any> KClass<T>.systemStyleConstructors(context: Context) = companionObject!!
    .functions
    .filter { it.returnType.isSubtypeOf(createType()) }
    .filterIsInstance<KFunction<T>>()
    .associate {
        it.name.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() } to
            it.carry(companionObjectInstance, context)
    }

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
