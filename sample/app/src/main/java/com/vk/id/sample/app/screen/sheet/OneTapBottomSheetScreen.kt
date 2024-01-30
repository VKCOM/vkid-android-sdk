package com.vk.id.sample.app.screen.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.vk.id.AccessToken
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapSheetCornersStyle
import com.vk.id.onetap.compose.onetap.sheet.style.rememberOneTapBottomSheetStyle
import com.vk.id.onetap.xml.OneTapBottomSheet
import com.vk.id.sample.app.screen.Button
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.app.uikit.selector.CheckboxSelector
import com.vk.id.sample.app.uikit.selector.DropdownSelector
import com.vk.id.sample.app.uikit.selector.EnumStateCheckboxSelector
import com.vk.id.sample.app.uikit.selector.styleConstructors
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback

@Preview
@Composable
@Suppress("LongMethod")
fun OneTapBottomSheetScreen() {
    val context = LocalContext.current
    val token: MutableState<AccessToken?> = remember { mutableStateOf(null) }
    val selectedScenario = rememberSaveable { mutableStateOf(OneTapScenario.EnterService) }
    val selectedStyle = rememberOneTapBottomSheetStyle(OneTapBottomSheetStyle.system(context))
    val autoHideSheetOnSuccess = rememberSaveable { mutableStateOf(true) }
    val selectedOAuths = rememberSaveable { mutableStateOf(setOf(OneTapOAuth.OK, OneTapOAuth.MAIL)) }
    val shouldUseXml = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        val bottomSheetState = rememberOneTapBottomSheetState()
        var bottomSheetView: OneTapBottomSheet? by remember { mutableStateOf(null) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (shouldUseXml.value) {
                AndroidView(factory = { context ->
                    OneTapBottomSheet(context).apply {
                        setCallbacks(
                            onAuth = getOneTapSuccessCallback(context) { token.value = it },
                            onFail = getOneTapFailCallback(context),
                        )
                        bottomSheetView = this
                    }
                })
                bottomSheetView?.oAuths = selectedOAuths.value
            } else {
                OneTapBottomSheet(
                    style = selectedStyle.value,
                    onAuth = getOneTapSuccessCallback(context) { token.value = it },
                    onFail = getOneTapFailCallback(context),
                    state = bottomSheetState,
                    scenario = selectedScenario.value,
                    autoHideOnSuccess = autoHideSheetOnSuccess.value,
                    serviceName = "VKID Sample",
                    oAuths = selectedOAuths.value,
                )
            }
        }
        DropdownSelector(
            values = enumValues<OneTapScenario>().associateBy { it.name },
            selectedValue = selectedScenario.value.name,
            onValueSelected = { selectedScenario.value = it }
        )
        DropdownSelector(
            values = OneTapBottomSheetStyle::class.styleConstructors(context),
            selectedValue = selectedStyle.value::class.simpleName ?: error("Can get simple style"),
            onValueSelected = {
                selectedStyle.value = it.call(
                    OneTapSheetCornersStyle.Default,
                    OneTapButtonCornersStyle.Default,
                    OneTapButtonSizeStyle.DEFAULT,
                )
            }
        )
        CheckboxSelector(
            title = "XML",
            isChecked = shouldUseXml.value,
            onCheckedChange = { shouldUseXml.value = it }
        )
        EnumStateCheckboxSelector(state = selectedOAuths)
        CheckboxSelector(
            title = "Auto hide on success",
            isChecked = autoHideSheetOnSuccess.value,
            onCheckedChange = { autoHideSheetOnSuccess.value = it }
        )
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
                bottomSheetView?.let { if (it.isVisible()) it.hide() else it.show() }
            }
            Spacer(Modifier.weight(1f))
        }
        token.value?.let {
            UseToken(accessToken = it)
        }
    }
}
