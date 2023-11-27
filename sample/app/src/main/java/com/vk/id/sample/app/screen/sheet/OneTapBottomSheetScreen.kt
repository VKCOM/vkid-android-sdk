package com.vk.id.sample.app.screen.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.sheet.style.rememberOneTapBottomSheetStyle
import com.vk.id.sample.app.screen.home.Button
import com.vk.id.sample.xml.uikit.common.onVKIDAuthFail
import com.vk.id.sample.xml.uikit.common.onVKIDAuthSuccess

@Preview
@Composable
fun OneTapBottomSheetScreen() {
    val context = LocalContext.current
    val selectedScenario = rememberSaveable { mutableStateOf(OneTapScenario.EnterService) }
    val selectedStyle = rememberOneTapBottomSheetStyle(
        OneTapBottomSheetStyle.Light()
    )
    val autoHideSheetOnSuccess = rememberSaveable { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val bottomSheetState = rememberOneTapBottomSheetState()
        OneTapBottomSheet(
            style = selectedStyle.value,
            onAuth = { onVKIDAuthSuccess(context, it) },
            onFail = { onVKIDAuthFail(context, it) },
            state = bottomSheetState,
            scenario = selectedScenario.value,
            autoHideOnSuccess = autoHideSheetOnSuccess.value,
            serviceName = "VKID Sample"
        )
        Selector("Scenario") {
            ScenarioMenu(selectedScenario)
        }
        Selector("Style") {
            StyleMenu(selectedStyle)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = autoHideSheetOnSuccess.value, onCheckedChange = { autoHideSheetOnSuccess.value = it })
            Text("Auto hide on success")
        }
        Spacer(Modifier.padding(16.dp))
        Row {
            Button(
                text = "Show",
                modifier = Modifier.width(100.dp)
            ) {
                if (bottomSheetState.isVisible) {
                    bottomSheetState.hide()
                } else {
                    bottomSheetState.show()
                }
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun Selector(name: String, content: @Composable RowScope.() -> Unit) {
    Row(
        Modifier.padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScenarioMenu(
    selectedScenario: MutableState<OneTapScenario>
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = selectedScenario.value.name,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { }
        ) {
            enumValues<OneTapScenario>().forEachIndexed { i, scenario ->
                DropdownMenuItem(
                    text = {
                        Text(scenario.name)
                    },
                    onClick = {
                        selectedScenario.value = scenario
                        expanded = false
                    }
                )
                if (i != OneTapScenario.values().size - 1) {
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StyleMenu(
    selectedStyle: MutableState<OneTapBottomSheetStyle>
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = selectedStyle.value::class.simpleName ?: "Never",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { }
        ) {
            val styles = remember {
                listOf(
                    "Light" to OneTapBottomSheetStyle.Light(),
                    "Dark" to OneTapBottomSheetStyle.Dark(),
                    "TransparentDark" to OneTapBottomSheetStyle.TransparentDark(),
                    "TransparentLight" to OneTapBottomSheetStyle.TransparentLight()
                )
            }
            styles.forEachIndexed { i, style ->
                DropdownMenuItem(
                    text = {
                        Text(style.first)
                    },
                    onClick = {
                        selectedStyle.value = style.second
                        expanded = false
                    }
                )
                if (i != styles.size - 1) {
                    Divider()
                }
            }
        }
    }
}
